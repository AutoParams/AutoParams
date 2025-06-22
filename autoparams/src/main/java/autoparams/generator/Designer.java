package autoparams.generator;

import java.util.HashMap;
import java.util.Map;

import autoparams.customization.Customizer;
import autoparams.customization.dsl.FunctionDelegate;

public final class Designer<T> {

    private final Class<T> type;
    private final Map<FunctionDelegate<T, ?>, Object> propertyBindings;

    Designer(Class<T> type) {
        this.type = type;
        this.propertyBindings = new HashMap<>();
    }

    public T create() {
        Factory<T> factory = Factory.create(type);

        Customizer[] customizers = propertyBindings.entrySet().stream()
            .map(entry -> {
                @SuppressWarnings("unchecked")
                FunctionDelegate<T, Object> getter =
                    (FunctionDelegate<T, Object>) entry.getKey();
                Object value = entry.getValue();
                return new Customizer() {
                    @Override
                    public ObjectGenerator customize(ObjectGenerator generator) {
                        ObjectGenerator freezeGenerator =
                            autoparams.customization.dsl.ArgumentCustomizationDsl
                                .set(getter).to(value);
                        return (query, context) -> {
                            ObjectContainer result = freezeGenerator.generate(query, context);
                            return result == autoparams.generator.ObjectContainer.EMPTY
                                ? generator.generate(query, context)
                                : result;
                        };
                    }
                };
            }).toArray(Customizer[]::new);

        return factory.get(customizers);
    }

    public <P> ParameterBinding<T, P> set(
        FunctionDelegate<T, P> getterDelegate
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("getterDelegate cannot be null");
        }

        return new ParameterBinding<T, P>(this, getterDelegate);
    }

    public static class ParameterBinding<T, P> {

        private final Designer<T> designer;
        private final FunctionDelegate<T, P> getterDelegate;

        private ParameterBinding(Designer<T> designer, FunctionDelegate<T, P> getterDelegate) {
            this.designer = designer;
            this.getterDelegate = getterDelegate;
        }

        public Designer<T> to(P value) {
            designer.propertyBindings.put(getterDelegate, value);
            return designer;
        }
    }
}
