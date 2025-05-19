package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.BiPredicate;

import autoparams.FieldQuery;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.internal.reflect.TypeLens;

/**
 * Matching strategies for use with the {@link FreezeBy} annotation to determine
 * which parameters or fields will receive a frozen value.
 * <p>
 * This enum provides different strategies to match parameters and queries,
 * allowing for flexible control over how frozen values are injected into other
 * parameters or fields in parameterized tests.
 * </p>
 *
 * @see FreezeBy
 * @see #EXACT_TYPE
 * @see #IMPLEMENTED_INTERFACES
 * @see #PARAMETER_NAME
 * @see #FIELD_NAME
 */
public enum Matching implements BiPredicate<Parameter, ObjectQuery> {

    /**
     * Matches parameters with exactly the same type.
     * <p>
     * This strategy matches a target parameter or field only if its type is
     * exactly the same as the frozen value's type. It uses Java's type equality
     * mechanism to perform the matching.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * In this example, both {@link String} parameters will receive the same
     * instance because they have the exact same type.
     * </p>
     * <pre>
     * &#64;Test
     * &#64;AutoParams
     * void test(&#64;FreezeBy(EXACT_TYPE) String value1, String value2) {
     *     assertSame(value1, value2);
     * }
     * </pre>
     */
    EXACT_TYPE {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            Type parameterType = parameter.getParameterizedType();
            return query.getType().equals(parameterType);
        }
    },

    /**
     * Matches parameters when the target type implements the frozen value's
     * interface.
     * <p>
     * This strategy matches a target parameter or field if its type implements
     * the interface of the frozen value's type. It's useful when you want to
     * inject the same instance into parameters that have an
     * interface-implementation relationship.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * In this example, a frozen {@link String} instance will be injected into
     * both the {@link String} and {@link CharSequence} parameters, as
     * {@link String} implements the {@link CharSequence} interface.
     * </p>
     * <pre>
     * &#64;Test
     * &#64;AutoParams
     * void test(
     *     &#64;FreezeBy(IMPLEMENTED_INTERFACES) String value1,
     *     CharSequence value2
     * ) {
     *     assertSame(value1, value2);
     * }
     * </pre>
     */
    IMPLEMENTED_INTERFACES {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            Type parameterType = parameter.getParameterizedType();
            TypeLens typeLens = new TypeLens(parameterType);
            return typeLens.implementsInterface(query.getType());
        }
    },

    /**
     * Matches parameters with the same parameter name.
     * <p>
     * This strategy matches a target parameter if its name is the same as the
     * frozen parameter's name. This is particularly useful when working with
     * domain objects that have constructor parameters with meaningful names.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * In this example, the {@code userId} parameter is frozen and injected into
     * the {@code userId} constructor parameter of the {@code User} class,
     * </p>
     * <pre>
     * &#64;Test
     * &#64;AutoParams
     * void test(&#64;FreezeBy(PARAMETER_NAME) UUID userId, User user) {
     *     assertSame(userId, user.getUserId());
     * }
     * </pre>
     */
    PARAMETER_NAME {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            return query instanceof ParameterQuery
                && test(parameter, (ParameterQuery) query);
        }

        private boolean test(Parameter parameter, ParameterQuery query) {
            String parameterName = query.getRequiredParameterName();
            return parameter.getName().equals(parameterName);
        }
    },

    /**
     * Matches parameters with the same field name.
     * <p>
     * This strategy matches a target parameter if its name is the same as a
     * field name in the class being constructed. This is useful when you want
     * to inject the same frozen value into fields with specific names.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * In this example, the {@code id} parameter is frozen and injected into the
     * field named {@code "id"} in the {@code Product} class.
     * </p>
     * <pre>
     * &#64;Test
     * &#64;AutoParams
     * &#64;WriteInstanceFields(Product.class)
     * void test(&#64;FreezeBy(FIELD_NAME) UUID id, Product product) {
     *     assertSame(id, product.getId());
     * }
     * </pre>
     *
     * @see WriteInstanceFields
     */
    FIELD_NAME {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            return query instanceof FieldQuery
                && test(parameter, (FieldQuery) query);
        }

        private boolean test(Parameter parameter, FieldQuery query) {
            String fieldName = query.getField().getName();
            return parameter.getName().equals(fieldName);
        }
    }
}
