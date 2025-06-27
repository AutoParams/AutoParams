package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Collection;

import autoparams.LogVisibility;

/**
 * Represents a strategy for extracting constructors from a given type.
 * <p>
 * This interface is used to define how constructors are selected or discovered
 * for a particular {@link Class Class&lt;?&gt;}. Implementations can provide
 * different mechanisms for constructor extraction, such as selecting public
 * constructors, constructors with specific annotations, or following other
 * custom rules.
 * </p>
 */
@LogVisibility(verboseOnly = true)
@FunctionalInterface
public interface ConstructorExtractor {

    /**
     * Extracts a collection of constructors from the specified type.
     * <p>
     * Implementations of this method should return a collection of
     * {@link Constructor Constructor&lt;?&gt;} for the given {@code type}. The
     * criteria for relevance can vary depending on the specific extraction
     * strategy.
     * </p>
     *
     * @param type the {@link Class Class&lt;?&gt;} from which to extract
     *             constructors.
     * @return a {@link Collection Collection&lt;E&gt;} of
     *         {@link Constructor Constructor&lt;?&gt;} objects. This collection
     *         may be empty if no suitable constructors are found, but it should
     *         not be {@code null}.
     */
    Collection<Constructor<?>> extract(Class<?> type);
}
