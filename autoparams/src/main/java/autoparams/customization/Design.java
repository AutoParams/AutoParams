package autoparams.customization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.dsl.FunctionDelegate;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.internal.reflect.Property;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Design<T> {

    private final Class<T> type;
    private final List<Customizer> customizers;

    private Design(Class<T> type) {
        this.type = type;
        this.customizers = emptyList();
    }

    private Design(Class<T> type, List<Customizer> customizers) {
        this.type = type;
        this.customizers = customizers;
    }

    public static <T> Design<T> of(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null");
        }

        return new Design<>(type);
    }

    public <P> Design<T> supply(
        FunctionDelegate<T, P> propertyGetter,
        Supplier<P> supplier
    ) {
        if (propertyGetter == null) {
            throw new IllegalArgumentException("The argument 'propertyGetter' must not be null");
        }

        if (supplier == null) {
            throw new IllegalArgumentException("The argument 'supplier' must not be null");
        }

        Property<T, P> property = Property.parse(propertyGetter);
        List<Customizer> nextCustomizers = new ArrayList<>(customizers);
        nextCustomizers.add(new ArgumentSupplier<>(property, supplier));
        return new Design<>(type, unmodifiableList(nextCustomizers));
    }

    public <P> Design<T> set(FunctionDelegate<T, P> propertyGetter, P value) {
        return supply(propertyGetter, () -> value);
    }

    public <P> Design<T> design(
        FunctionDelegate<T, P> propertyGetter,
        Function<Design<P>, Design<P>> designFunction
    ) {
        if (propertyGetter == null) {
            throw new IllegalArgumentException("The argument 'propertyGetter' must not be null");
        }

        if (designFunction == null) {
            throw new IllegalArgumentException("The argument 'designFunction' must not be null");
        }

        Property<T, P> property = Property.parse(propertyGetter);
        List<Customizer> nextCustomizers = new ArrayList<>(customizers);
        nextCustomizers.add(new ArgumentDesigner<>(property, designFunction));
        return new Design<>(type, unmodifiableList(nextCustomizers));
    }

    public T instantiate() {
        ResolutionContext context = new ResolutionContext();
        context.customize(customizers.toArray(new Customizer[0]));
        return context.resolve(type);
    }

    public T instantiate(ResolutionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers.toArray(new Customizer[0]));
        return branch.resolve(type);
    }

    public List<T> instantiate(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(instantiate());
        }
        return unmodifiableList(result);
    }

    private abstract static class AbstractArgumentGenerator<T, P>
        implements ObjectGenerator {

        protected final Property<T, P> property;

        public AbstractArgumentGenerator(Property<T, P> property) {
            this.property = property;
        }

        @Override
        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return matches(query)
                ? generate()
                : ObjectContainer.EMPTY;
        }

        protected abstract ObjectContainer generate();

        private boolean matches(ObjectQuery query) {
            return query instanceof ParameterQuery
                && matches((ParameterQuery) query);
        }

        private boolean matches(ParameterQuery query) {
            return matchesParameterType(query)
                && matchesParameterName(query)
                && matchesDeclaringClass(query);
        }

        private boolean matchesParameterType(ParameterQuery query) {
            return property.getType().equals(query.getType());
        }

        private boolean matchesParameterName(ParameterQuery query) {
            return property.getName().equals(query.getRequiredParameterName());
        }

        private boolean matchesDeclaringClass(ParameterQuery query) {
            return property.getDeclaringClass().equals(
                query
                    .getParameter()
                    .getDeclaringExecutable()
                    .getDeclaringClass()
            );
        }
    }

    private static class ArgumentSupplier<T, P>
        extends AbstractArgumentGenerator<T, P> {

        private final Supplier<P> supplier;

        public ArgumentSupplier(Property<T, P> property, Supplier<P> supplier) {
            super(property);
            this.supplier = supplier;
        }

        @Override
        protected ObjectContainer generate() {
            return new ObjectContainer(supplier.get());
        }
    }

    private static class ArgumentDesigner<T, P>
        extends AbstractArgumentGenerator<T, P> {

        private final Function<Design<P>, Design<P>> designFunction;

        public ArgumentDesigner(
            Property<T, P> property,
            Function<Design<P>, Design<P>> designFunction
        ) {
            super(property);
            this.designFunction = designFunction;
        }

        @Override
        protected ObjectContainer generate() {
            P value = designFunction
                .apply(Design.of(property.getType()))
                .instantiate();
            return new ObjectContainer(value);
        }
    }
}
