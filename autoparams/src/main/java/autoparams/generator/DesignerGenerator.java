package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Designer;

class DesignerGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        Type type = query.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals(Designer.class)) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length == 1) {
                    Type targetType = typeArguments[0];
                    if (targetType instanceof Class) {
                        Class<?> targetClass = (Class<?>) targetType;
                        return new ObjectContainer(Designer.design(targetClass));
                    }
                }
            }
        }
        return ObjectContainer.EMPTY;
    }
}
