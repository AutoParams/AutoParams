package autoparams.customization.dsl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.type.TypeReference;

import static autoparams.customization.dsl.GetterDelegate.getGetterOf;
import static autoparams.customization.dsl.ParameterNameInferencer.inferParameterNameFromGetter;

/**
 * A DSL(domain-specific language) for customizing parameter resolution in
 * AutoParams.
 * <p>
 * This class provides a set of static factory methods that create
 * {@link FreezeArgument} instances with different selection criteria. The DSL
 * enables fluent and expressive configuration of parameter customization in
 * tests.
 * </p>
 *
 * <p><b>Example: Freezing a specific parameter</b></p>
 * <p>
 * This example shows how to fix the value of all String parameters named
 * "name":
 * </p>
 * <pre>
 * freezeArgument("name")
 *     .where(parameterTypeMatches(String.class))
 *     .to("John Doe")
 * </pre>
 *
 * <p><b>Example: Setting a parameter using method reference</b></p>
 * <p>
 * This example shows how to set the product parameter of a Review to a specific
 * value:
 * </p>
 * <pre>
 * set(Review::getProduct).to(product)
 * </pre>
 *
 * @see FreezeArgument
 * @see ParameterQueryDsl
 */
public final class ArgumentCustomizationDsl {

    private ArgumentCustomizationDsl() {
    }

    /**
     * Creates a new {@link FreezeArgument} with no initial parameter selection
     * criteria.
     * <p>
     * This method returns a {@link FreezeArgument} instance that starts with no
     * constraints on parameter selection. You can add specific criteria by
     * chaining {@link FreezeArgument#where(Predicate)} calls to refine the
     * selection before applying a fixed value with
     * {@link FreezeArgument#to(Object)}.
     * </p>
     *
     * <p><b>Example: Freezing all String parameters</b></p>
     * <p>
     * This example shows how to freeze all String parameters to a specific
     * value:
     * </p>
     * <pre>
     * freezeArgument()
     *     .where(parameterTypeMatches(String.class))
     *     .to("Default Value")
     * </pre>
     *
     * @return a new {@link FreezeArgument} with no initial parameter selection
     *         criteria
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     * @see ParameterQueryDsl
     */
    public static FreezeArgument freezeArgument() {
        return new FreezeArgument(new ReturnTrue<>());
    }

    /**
     * Creates a new {@link FreezeArgument} with a custom parameter selection
     * predicate.
     * <p>
     * This method allows you to specify a custom {@link Predicate} that
     * determines which parameters should be selected for value freezing.
     * It provides maximum flexibility for complex selection criteria.
     * </p>
     *
     * <p><b>Example: Using a custom predicate</b></p>
     * <p>
     * This example shows how to use a custom predicate to target specific
     * parameters:
     * </p>
     * <pre>
     * Predicate&lt;ParameterQuery&gt; customPredicate = query -&gt;
     *     query.getRequiredParameterName().startsWith("username") &amp;&amp;
     *     query.getType() == String.class;
     *
     * freezeArgument(customPredicate).to("Default User")
     * </pre>
     *
     * @param predicate a {@link Predicate} that defines which parameters to
     *                  select
     * @return a new {@link FreezeArgument} with the specified selection
     *         predicate
     * @see FreezeArgument#to(Object)
     * @see ParameterQuery#getRequiredParameterName()
     * @see ParameterQueryDsl
     */
    public static FreezeArgument freezeArgument(
        Predicate<ParameterQuery> predicate
    ) {
        return new FreezeArgument(predicate);
    }

    /**
     * Creates a new {@link FreezeArgument} that selects parameters by name.
     * <p>
     * This method returns a {@link FreezeArgument} that will select parameters
     * with the specified name. It provides a convenient way to select specific
     * parameters based on their name without constructing a predicate manually.
     * </p>
     *
     * <p><b>Example: Freezing parameters by name</b></p>
     * <p>
     * This example shows how to freeze all parameters named "username" to a
     * specific value:
     * </p>
     * <pre>
     * freezeArgument("username").to("admin")
     * </pre>
     *
     * @param parameterName the name of parameters to select
     * @return a new {@link FreezeArgument} that selects parameters with the
     *         specified name
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     */
    public static FreezeArgument freezeArgument(String parameterName) {
        return new FreezeArgument(new ParameterNameEquals(parameterName));
    }

    /**
     * Creates a new {@link FreezeArgument} that selects parameters by both type
     * and name.
     * <p>
     * This method returns a {@link FreezeArgument} that will select parameters
     * with the specified type and name. This allows for more precise targeting
     * compared to selecting by name or type alone.
     * </p>
     *
     * <p><b>Example: Freezing parameters by type and name</b></p>
     * <p>
     * This example shows how to freeze String parameters named "email" to a
     * specific value:
     * </p>
     * <pre>
     * freezeArgument(String.class, "email").to("test@example.com")
     * </pre>
     *
     * @param parameterType the type of parameters to select
     * @param parameterName the name of parameters to select
     * @return a new {@link FreezeArgument} that selects parameters with the
     *         specified type and name
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     */
    public static FreezeArgument freezeArgument(
        Type parameterType,
        String parameterName
    ) {
        return new FreezeArgument(
            new ParameterTypeMatches(parameterType)
                .and(new ParameterNameEquals(parameterName))
        );
    }

    /**
     * Creates a new {@link FreezeArgument} that selects parameters by type
     * reference and name.
     * <p>
     * This method provides a type-safe alternative to
     * {@link #freezeArgument(Type, String)} when working with generic types.
     * The {@link TypeReference} allows capturing generic type information that
     * would otherwise be lost due to type erasure.
     * </p>
     *
     * <p><b>Example: Freezing generic parameters</b></p>
     * <p>
     * This example shows how to freeze {@code List<String>} parameters named
     * "items" to a specific value:
     * </p>
     * <pre>
     * List&lt;String&gt; items = Arrays.asList("item1", "item2");
     * freezeArgument(new TypeReference&lt;List&lt;String&gt;&gt;() { }, "items").to(items)
     * </pre>
     *
     * @param <T> the type of parameter to select
     * @param parameterTypeReference a type reference capturing the generic type
     *                               to match
     * @param parameterName the name of parameters to select
     * @return a new {@link FreezeArgument} that selects parameters with the
     *         specified type and name
     * @see #freezeArgument(Type, String)
     * @see TypeReference
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     */
    public static <T> FreezeArgument freezeArgument(
        TypeReference<T> parameterTypeReference,
        String parameterName
    ) {
        return freezeArgument(parameterTypeReference.getType(), parameterName);
    }

    /**
     * Creates a new {@link FreezeArgument} using a method reference to a getter
     * method.
     * <p>
     * This method enables a type-safe way to select parameters by inferring the
     * parameter type, name, and declaring class from the provided getter method
     * reference. It is useful for selecting parameters of constructors or
     * setter methods in classes with well-defined getter methods, such as
     * JavaBeans or records.
     * </p>
     *
     * <p><b>Example: Setting parameters using method reference</b></p>
     * <p>
     * This example shows how to set the product parameter of a Review to a
     * specific value:
     * </p>
     * <pre>
     * set(Review::getProduct).to(product)
     * </pre>
     *
     * @param <T> the type containing the getter method
     * @param <P> the return type of the getter method
     * @param getterDelegate a method reference to a getter method
     * @return a new {@link FreezeArgument} that selects parameters based on the
     *         getter method
     * @see FunctionDelegate
     * @see FreezeArgument#to(Object)
     */
    public static <T, P> FreezeArgument set(
        FunctionDelegate<T, P> getterDelegate
    ) {
        Method getter = getGetterOf(getterDelegate);
        String parameterName = inferParameterNameFromGetter(getter);
        return new FreezeArgument(
            new ParameterTypeMatches(getter.getReturnType())
                .and(new ParameterNameEquals(parameterName))
                .and(new DeclaringClassEquals(getter.getDeclaringClass()))
        );
    }

    /**
     * Creates a new {@link FreezeArgument} that selects parameters by type.
     * <p>
     * This method returns a {@link FreezeArgument} that initially selects
     * parameters with the specified type. After creating the initial selection,
     * you can add more conditions by chaining
     * {@link FreezeArgument#where(Predicate)} calls.
     * </p>
     *
     * <p><b>Example: Freezing parameters by type</b></p>
     * <p>
     * This example shows how to freeze all String parameters to a specific
     * value:
     * </p>
     * <pre>
     * freezeArgumentOf(String.class).to("hello world")
     * </pre>
     *
     * @param parameterType the type of parameters to select
     * @return a new {@link FreezeArgument} that selects parameters of the
     *         specified type
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     */
    public static FreezeArgument freezeArgumentOf(Type parameterType) {
        return new FreezeArgument(new ParameterTypeMatches(parameterType));
    }

    /**
     * Creates a new {@link FreezeArgument} that selects parameters by generic
     * type reference.
     * <p>
     * This method provides a type-safe alternative to
     * {@link #freezeArgumentOf(Type)} when working with generic types.
     * The {@link TypeReference} allows capturing generic type information that
     * would otherwise be lost due to type erasure.
     * </p>
     *
     * <p><b>Example: Freezing parameters with generic types</b></p>
     * <p>
     * This example shows how to freeze all {@code List<String>} parameters to a
     * specific value:
     * </p>
     * <pre>
     * List&lt;String&gt; items = Arrays.asList("item1", "item2");
     * freezeArgumentOf(new TypeReference&lt;List&lt;String&gt;&gt;() { }).to(items)
     * </pre>
     *
     * @param <T> the type of parameter to select
     * @param parameterTypeReference a type reference capturing the generic type
     *                               to match
     * @return a new {@link FreezeArgument} that selects parameters of the
     *         specified type
     * @see #freezeArgumentOf(Type)
     * @see TypeReference
     * @see FreezeArgument#where(Predicate)
     * @see FreezeArgument#to(Object)
     */
    public static <T> FreezeArgument freezeArgumentOf(
        TypeReference<T> parameterTypeReference
    ) {
        return freezeArgumentOf(parameterTypeReference.getType());
    }
}
