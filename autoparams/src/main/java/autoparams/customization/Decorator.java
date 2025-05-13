package autoparams.customization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.generator.ObjectGenerator;

public abstract class Decorator<T> implements Customizer {

    private final Type componentType;

    protected Decorator() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType reference = (ParameterizedType) superClass;
        Type[] typeArguments = reference.getActualTypeArguments();
        componentType = typeArguments[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            if (query.getType().equals(componentType)) {
                return generator
                    .generate(query, context)
                    .process(component -> decorate((T) component));
            } else {
                return generator.generate(query, context);
            }
        };
    }

    protected abstract T decorate(T component);
}
