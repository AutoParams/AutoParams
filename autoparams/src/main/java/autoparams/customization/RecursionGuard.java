package autoparams.customization;

import java.lang.reflect.Type;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

public final class RecursionGuard implements Customizer {

    public static final int DEFAULT_RECURSION_DEPTH = 1;

    private static final ConcurrentMap<Object, RecursionContext> STORE;

    static {
        STORE = new ConcurrentHashMap<>();
    }

    private final int recursionDepth;

    public RecursionGuard(int recursionDepth) {
        this.recursionDepth = recursionDepth;
    }

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
