package autoparams.customization;

import java.beans.ConstructorProperties;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that instructs AutoParams to aggressively select the constructor
 * with the most parameters for specified target type(s).
 * <p>
 * When creating an instance of a class that has multiple constructors,
 * AutoParams typically follows a default policy (e.g., prioritizing
 * constructors annotated with
 * {@link ConstructorProperties @ConstructorProperties}, then choosing the one
 * with the fewest parameters). This annotation overrides that policy for the
 * specified types, forcing AutoParams to use the constructor that accepts the
 * largest number of arguments (the "greediest" one).
 * </p>
 * <p>
 * This annotation can be applied to a test method, a parameter, or another
 * annotation. When applied, it influences how instances of the specified
 * target types are created within the applicable scope.
 * </p>
 * <p>
 * This is particularly useful when you want to ensure that complex objects are
 * instantiated using their most comprehensive constructor for test data
 * generation, even if simpler constructors are available.
 * </p>
 * <p>
 * Contrast this with
 * {@link SelectGreediestConstructor @SelectGreediestConstructor}, which is
 * applied directly to a parameter and affects only that parameter's type.
 * {@link ResolveConstructorAggressively @ResolveConstructorAggressively}
 * provides more flexibility by allowing direct specification of the target
 * type(s) for this aggressive constructor selection policy.
 * </p>
 *
 * @see #value()
 * @see CustomizerSource
 * @see SelectGreediestConstructor
 * @see autoparams.AutoParams
 */
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(AggressiveConstructorResolutionCustomizerFactory.class)
public @interface ResolveConstructorAggressively {

    /**
     * Specifies the target class(es) for which the greediest constructor
     * selection policy should be applied.
     * <p>
     * AutoParams will use the constructor with the most parameters for any
     * type listed in this array when generating instances of those types
     * within the scope where this annotation is applied.
     * </p>
     *
     * @return an array of {@link Class} objects representing the target types.
     */
    Class<?>[] value();
}
