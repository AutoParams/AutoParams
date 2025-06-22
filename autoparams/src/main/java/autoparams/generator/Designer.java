package autoparams.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import autoparams.customization.Customizer;
import autoparams.customization.dsl.FunctionDelegate;

public final class Designer<T> {

    private final Class<T> type;
    private final List<PropertyEntry<T, ?>> propertyBindings;
    private final List<Consumer<T>> processors;

    Designer(Class<T> type) {
        this.type = type;
        this.propertyBindings = new ArrayList<>();
        this.processors = new ArrayList<>();
    }

    public T create() {
        Factory<T> factory = Factory.create(type);

        Customizer[] customizers = propertyBindings.stream()
            .map(entry -> {
                @SuppressWarnings("unchecked")
                FunctionDelegate<T, Object> getter =
                    (FunctionDelegate<T, Object>) entry.getterDelegate;
                Object value = entry.value;
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

        T object = factory.get(customizers);
        
        for (Consumer<T> processor : processors) {
            processor.accept(object);
        }
        
        return object;
    }

    public <P> ParameterBinding<T, P> set(
        FunctionDelegate<T, P> getterDelegate
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("getterDelegate cannot be null");
        }

        return new ParameterBinding<T, P>(this, getterDelegate);
    }

    public Designer<T> process(Consumer<T> processor) {
        if (processor == null) {
            throw new IllegalArgumentException("processor cannot be null");
        }

        this.processors.add(processor);
        return this;
    }

    private static class PropertyEntry<T, P> {
        final FunctionDelegate<T, P> getterDelegate;
        final Object value;

        PropertyEntry(FunctionDelegate<T, P> getterDelegate, Object value) {
            this.getterDelegate = getterDelegate;
            this.value = value;
        }
    }

    public static class ParameterBinding<T, P> {

        private final Designer<T> designer;
        private final FunctionDelegate<T, P> getterDelegate;

        private ParameterBinding(Designer<T> designer, FunctionDelegate<T, P> getterDelegate) {
            this.designer = designer;
            this.getterDelegate = getterDelegate;
        }

        public Designer<T> to(P value) {
            designer.propertyBindings.removeIf(entry -> 
                entry.getterDelegate.getClass().equals(getterDelegate.getClass()));
            
            designer.propertyBindings.add(new PropertyEntry<>(getterDelegate, value));
            return designer;
        }
    }
}
