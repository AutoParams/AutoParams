package autoparams.customization;

import java.util.ArrayList;
import java.util.List;
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

    public T instantiate() {
        ResolutionContext context = new ResolutionContext();
        context.customize(customizers.toArray(new Customizer[0]));
        return context.resolve(type);
    }

    private static class ArgumentSupplier<T, P> implements ObjectGenerator {

        private final Property<T, P> property;
        private final Supplier<P> supplier;

        public ArgumentSupplier(Property<T, P> property, Supplier<P> supplier) {
            this.property = property;
            this.supplier = supplier;
        }

        @Override
        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return matches(query)
                ? new ObjectContainer(supplier.get())
                : ObjectContainer.EMPTY;
        }

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
}
