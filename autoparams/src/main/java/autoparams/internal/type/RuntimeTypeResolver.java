package autoparams.internal.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public final class RuntimeTypeResolver {

    private final Map<TypeVariable<?>, Type> map;

    private RuntimeTypeResolver(Map<TypeVariable<?>, Type> map) {
        this.map = map;
    }

    public static RuntimeTypeResolver create(Type rootType) {
        return new RuntimeTypeResolver(buildMap(rootType));
    }

    private static Map<TypeVariable<?>, Type> buildMap(Type rootType) {
        return rootType instanceof ParameterizedType
            ? buildMap((ParameterizedType) rootType)
            : Collections.emptyMap();
    }

    private static Map<TypeVariable<?>, Type> buildMap(
        ParameterizedType rootType
    ) {
        Class<?> rawType = (Class<?>) rootType.getRawType();
        TypeVariable<?>[] parameters = rawType.getTypeParameters();
        Type[] arguments = rootType.getActualTypeArguments();
        return IntStream
            .range(0, parameters.length)
            .boxed()
            .collect(toMap(i -> parameters[i], i -> arguments[i]));
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
        return new ParameterizedTypeDescriptor(
            Arrays
                .stream(parameterizedType.getActualTypeArguments())
                .map(this::resolve)
                .toArray(Type[]::new),
            resolve(parameterizedType.getRawType()),
            resolve(parameterizedType.getOwnerType())
        );
    }
}
