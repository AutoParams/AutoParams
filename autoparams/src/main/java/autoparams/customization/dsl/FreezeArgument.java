package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.generator.ObjectGenerator;

/**
 * A DSL(Domain-Specific Language) component used to selectively freeze values
 * based on parameter criteria.
 * <p>
 * This class is part of the AutoParams DSL for customizing object generation.
 * It provides a fluent API for defining which parameters should receive a
 * frozen value during the resolution process. Parameters can be selected based
 * on name, type, declaring class, or other custom predicates.
 * </p>
 * <p>
 * Through method chaining, this class allows you to build complex parameter
 * selection rules. Once the target parameters are selected, they can be
 * associated with a specific value using the {@link #to(Object)} method.
 * </p>
 * <p>
 * This class can be used with {@link ParameterQueryDsl} predicates to create
 * powerful parameter selection criteria. For example:
 * </p>
 * <pre>
 * import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
 * import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEquals;
 * import static autoparams.customization.dsl.ParameterQueryDsl.parameterTypeMatches;
 * import static autoparams.customization.dsl.ParameterQueryDsl.declaringClassEquals;
 *
 * public class TestClass {
 *
 *     &#64;Test
 *     &#64;AutoParams
 *     void testMethod() {
 *         // Create a argument generator for String parameters named "name" in the User class
 *         ObjectGenerator generator = freezeArgument()
 *             .where(parameterNameEquals("name"))
 *             .where(parameterTypeMatches(String.class))
 *             .in(User.class)
 *             .to("John Doe");
 *     }
 * }
 * </pre>
 *
 * @see ArgumentCustomizationDsl
 * @see ParameterQueryDsl
 */
public final class FreezeArgument {

    private final Predicate<ParameterQuery> predicate;

    FreezeArgument(Predicate<ParameterQuery> predicate) {
        this.predicate = predicate;
    }

    /**
     * Adds an additional condition to filter parameters for value freezing.
     * <p>
     * This method creates a new {@link FreezeArgument} instance by combining
     * the current predicate with a new one using logical AND operation. This
     * enables step-by-step refinement of parameter selection criteria through
     * method chaining.
     * </p>
     *
     * <p><b>Example: Combining predicates for parameter selection</b></p>
     * <p>
     * This example shows how to select parameters named "name" of type String
     * declared in the User class:
     * </p>
     * <pre>
     * freezeArgument()
     *     .where(parameterNameEquals("name"))
     *     .where(parameterTypeMatches(String.class))
     *     .where(declaringClassEquals(User.class))
     * </pre>
     *
     * @param predicate the condition to add to the current selection criteria
     * @return a new {@link FreezeArgument} with the combined selection
     *         predicates
     * @see ArgumentCustomizationDsl#freezeArgument()
     * @see ParameterQueryDsl#parameterNameEquals(String)
     * @see ParameterQueryDsl#parameterTypeMatches(Type)
     * @see ParameterQueryDsl#declaringClassEquals(Class)
     */
    public FreezeArgument where(Predicate<ParameterQuery> predicate) {
        return new FreezeArgument(this.predicate.and(predicate));
    }

    /**
     * Constrains parameter selection to those declared in the specified class.
     * <p>
     * This method is convenient shorthand for
     * {@code where(new DeclaringClassEquals(declaringClass))}. It creates a new
     * {@link FreezeArgument} instance that only selects parameters declared
     * within the specified class.
     * </p>
     *
     * <p><b>Example: Freezing String parameters in User class</b></p>
     * <p>
     * This example shows how to freeze all String parameters in the User class
     * to a fixed value:
     * </p>
     * <pre>
     * ObjectGenerator generator = freezeArgument()
     *     .where(parameterTypeMatches(String.class))
     *     .in(User.class)
     *     .to("Default Value")
     * </pre>
     *
     * @param declaringClass the class in which the parameters must be declared
     * @return a new {@link FreezeArgument} with the added declaration class
     *         constraint
     * @see ArgumentCustomizationDsl#freezeArgument()
     * @see #where(Predicate)
     * @see ParameterQueryDsl#parameterTypeMatches(Type)
     * @see #to(Object)
     * @see DeclaringClassEquals
     */
    public FreezeArgument in(Class<?> declaringClass) {
        return where(new DeclaringClassEquals(declaringClass));
    }

    /**
     * Finalizes the parameter selection and assigns a fixed value to matching
     * parameters.
     * <p>
     * This method completes the parameter freezing process by associating the
     * specified value with all parameters that match the selection criteria
     * defined by previous method calls. It returns an {@link ObjectGenerator}
     * that can be used to customize the resolution context.
     * </p>
     *
     * <p><b>Example: Freezing String parameters in User class</b></p>
     * <p>
     * This example shows how to freeze all String parameters in the User class
     * to a fixed value:
     * </p>
     * <pre>
     * ObjectGenerator generator = freezeArgument()
     *     .where(parameterTypeMatches(String.class))
     *     .in(User.class)
     *     .to("Default Value")
     * </pre>
     *
     * @param value the fixed value to assign to all matching parameters
     * @return an {@link ObjectGenerator} that implements the parameter freezing
     * @see ArgumentCustomizationDsl#freezeArgument()
     * @see #where(Predicate)
     * @see ParameterQueryDsl#parameterTypeMatches(Type)
     * @see #in(Class)
     */
    public ObjectGenerator to(Object value) {
        return new FrozenArgumentGenerator(predicate, value);
    }
}
