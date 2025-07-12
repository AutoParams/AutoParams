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
import lombok.AllArgsConstructor;

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
        FunctionDelegate<T, P> getterDelegate,
        Supplier<P> supplier
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' must not be null");
        }

        if (supplier == null) {
            throw new IllegalArgumentException("The argument 'supplier' must not be null");
        }

        Property<T, P> property = Property.parse(getterDelegate);
        List<Customizer> nextCustomizers = new ArrayList<>(customizers);
        nextCustomizers.add(new ArgumentSupplier<>(property, supplier));
        return new Design<>(type, unmodifiableList(nextCustomizers));
    }

    public <P> Design<T> set(FunctionDelegate<T, P> getterDelegate, P value) {
        return supply(getterDelegate, () -> value);
    }

    public <P> Design<T> design(
        FunctionDelegate<T, P> getterDelegate,
        Function<Design<P>, Design<P>> designFunction
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' must not be null");
        }

        if (designFunction == null) {
            throw new IllegalArgumentException("The argument 'designFunction' must not be null");
        }

        return supply(getterDelegate, () -> {
            Property<T, P> property = Property.parse(getterDelegate);
            Design<P> design = Design.of(property.getType());
            return designFunction.apply(design).instantiate();
        });
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

    public List<T> instantiate(int count, ResolutionContext context) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers.toArray(new Customizer[0]));
        List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(branch.resolve(type));
        }
        return unmodifiableList(result);
    }

    @AllArgsConstructor
    private static class ArgumentSupplier<T, P> implements ObjectGenerator {

        private final Property<T, P> property;
        private final Supplier<P> supplier;

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
