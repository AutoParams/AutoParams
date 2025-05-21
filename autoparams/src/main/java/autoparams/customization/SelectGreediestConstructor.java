package autoparams.customization;

import java.beans.ConstructorProperties;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation, applied to a test method parameter, that instructs AutoParams
 * to select the constructor with the most parameters when creating an instance
 * of that parameter's type.
 * <p>
 * When a class has multiple constructors, AutoParams by default follows a
 * specific policy for constructor selection (e.g., prioritizing constructors
 * annotated with {@link ConstructorProperties @ConstructorProperties}, then
 * choosing the one with the fewest parameters). Applying
 * {@link SelectGreediestConstructor @SelectGreediestConstructor} to a parameter
 * changes this behavior for the parameter's type, causing AutoParams to always
 * select the constructor with the most parameters.
 * </p>
 * <p>
 * This can be useful in scenarios where a type has a primary, more
 * comprehensive constructor that should be used for test data generation, even
 * if other, simpler constructors exist.
 * </p>
 * <p>
 * This annotation should be distinguished from
 * {@link ResolveConstructorAggressively @ResolveConstructorAggressively}.
 * While both annotations instruct AutoParams to select the constructor with
 * the most parameters (the "greediest" one), their key difference lies in
 * their application and scope.
 * {@code @SelectGreediestConstructor} is applied to a specific parameter,
 * and its effect is localized to the type of that annotated parameter.
 * {@link ResolveConstructorAggressively @ResolveConstructorAggressively},
 * on the other hand, allows you to explicitly specify the target type(s)
 * to which the greediest constructor selection policy should apply,
 * offering a potentially broader or more targeted scope of influence.
 * </p>
 *
 * @see CustomizerSource
 * @see ResolveConstructorAggressively
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@CustomizerSource(GreediestConstructorCustomizerFactory.class)
public @interface SelectGreediestConstructor {
}
