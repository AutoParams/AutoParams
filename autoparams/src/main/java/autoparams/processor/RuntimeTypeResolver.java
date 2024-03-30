package autoparams.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class RuntimeTypeResolver {

    private final Map<TypeVariable<?>, Type> map;

    private RuntimeTypeResolver(Map<TypeVariable<?>, Type> map) {
        this.map = map;
    }

    public static RuntimeTypeResolver of(Type source) {
        return new RuntimeTypeResolver(source instanceof ParameterizedType
            ? buildMap((ParameterizedType) source)
            : Collections.emptyMap());
    }

    private static Map<TypeVariable<?>, Type> buildMap(
        ParameterizedType parameterizedType
    ) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        TypeVariable<?>[] typeVariables = rawType.getTypeParameters();
        Type[] typeValues = parameterizedType.getActualTypeArguments();

        HashMap<TypeVariable<?>, Type> map = new HashMap<>();
        for (int i = 0; i < typeVariables.length; i++) {
            map.put(typeVariables[i], typeValues[i]);
        }

        return map;
    }

    public Type resolve(Type type) {
        if (type instanceof TypeVariable<?> && map.containsKey(type)) {
            return map.get(type);
        } else if (type instanceof ParameterizedType) {
            return resolve((ParameterizedType) type);
        } else {
            return type;
        }
    }

    private Type resolve(ParameterizedType parameterizedType) {
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < typeArguments.length; i++) {
            typeArguments[i] = resolve(typeArguments[i]);
        }

        return new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                return typeArguments.clone();
            }

            @Override
            public Type getRawType() {
                return resolve(parameterizedType.getRawType());
            }

            @Override
            public Type getOwnerType() {
                return resolve(parameterizedType.getOwnerType());
            }
        };
    }
}
