package autoparams.customization;

import java.lang.reflect.Type;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

/**
 * A {@link Customizer} that prevents infinite recursion during object
 * generation.
 * <p>
 * When generating complex objects, especially those with circular dependencies
 * or self-referential types, there's a risk of an {@link ObjectGenerator}
 * entering an infinite loop. This customizer guards against such scenarios by
 * monitoring the recursion depth for each type being generated. If the depth
 * exceeds a predefined limit (by default, {@link #DEFAULT_RECURSION_DEPTH}),
 * the guard intervenes and typically returns a null, thus breaking the
 * recursion.
 * </p>
 * <p>
 * This mechanism ensures that the test data generation process remains stable
 * even in the presence of recursive type definitions.
 * </p>
 *
 * @see Customizer
 * @see ObjectGenerator
 */
public final class RecursionGuard implements Customizer {

    /**
     * The default limit for recursion depth when generating objects. The value
     * is {@code 1}.
     *
     * @see #RecursionGuard()
     * @see #RecursionGuard(int)
     */
    public static final int DEFAULT_RECURSION_DEPTH = 1;

    private static final ConcurrentMap<Object, RecursionContext> STORE;

    static {
        STORE = new ConcurrentHashMap<>();
    }

    private final int recursionDepth;

    /**
     * Constructs a {@link RecursionGuard} with a specific recursion depth limit.
     *
     * @param recursionDepth the maximum number of times a type can be
     *                       recursively generated. Must be a positive integer.
     * @see #RecursionGuard()
     */
    public RecursionGuard(int recursionDepth) {
        this.recursionDepth = recursionDepth;
    }

    /**
     * Constructs a {@link RecursionGuard} with the default recursion depth
     * limit.
     * <p>
     * This constructor initializes the guard with
     * {@link #DEFAULT_RECURSION_DEPTH}.
     * </p>
     *
     * @see #DEFAULT_RECURSION_DEPTH
     * @see #RecursionGuard(int)
     */
    public RecursionGuard() {
        this(DEFAULT_RECURSION_DEPTH);
    }

    private class RecursionContext {

        public final Stack<Type> monitor = new Stack<>();
        public final RecursionGuard guard;
        public final Object scope;

        public RecursionContext(Object scope) {
            this.guard = RecursionGuard.this;
            this.scope = scope;
        }
    }

    /**
     * Customizes the provided {@link ObjectGenerator} to add recursion
     * protection.
     * <p>
     * This method returns a new {@link ObjectGenerator} that wraps the original
     * generator. The new generator monitors the recursion depth for each
     * requested type during the object generation process. If the depth for a
     * specific type exceeds the {@code recursionDepth} configured for this
     * {@code RecursionGuard} (see {@link #RecursionGuard(int)} and
     * {@link #DEFAULT_RECURSION_DEPTH}), the generation for that type is
     * interrupted. In such cases, an {@link ObjectContainer} holding
     * {@code null} is returned to break the recursion and prevent a
     * {@link StackOverflowError}.
     * </p>
     * <p>
     * The recursion tracking is managed per resolution context, ensuring that
     * concurrent or nested resolution processes maintain their own recursion
     * counts.
     * </p>
     *
     * @param generator the original {@link ObjectGenerator} to be customized.
     * @return a new {@link ObjectGenerator} equipped with recursion guarding
     *         capabilities.
     */
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            final Object scope = new Object();

            final RecursionContext recursionContext = STORE.computeIfAbsent(
                context,
                x -> new RecursionContext(scope)
            );

            try {
                if (recursionContext.guard.equals(this) == false) {
                    return generator.generate(query, context);
                }

                final Stack<Type> monitor = recursionContext.monitor;
                final Type type = query.getType();

                monitor.push(type);
                try {
                    final long depth = monitor
                        .stream()
                        .filter(x -> x.equals(type))
                        .count();
                    return depth > recursionDepth
                        ? new ObjectContainer(null)
                        : generator.generate(query, context);
                } finally {
                    monitor.pop();
                }
            } finally {
                if (recursionContext.scope.equals(scope)) {
                    STORE.remove(context);
                }
            }
        };
    }
}
