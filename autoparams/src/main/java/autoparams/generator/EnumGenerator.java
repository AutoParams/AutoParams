package autoparams.generator;

import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class EnumGenerator implements ObjectGenerator {

    private final Map<Class<?>, Object[]> cache = new ConcurrentHashMap<>();

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Type type = query.getType();
        return isEnum(type)
            ? new ObjectContainer(generateValue((Class<?>) type))
            : ObjectContainer.EMPTY;
    }

    private static boolean isEnum(Type type) {
        return type instanceof Class
            && Enum.class.isAssignableFrom((Class<?>) type);
    }

    private Object generateValue(Class<?> enumType) {
        Object[] values = getValues(enumType);
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object[] getValues(Class<?> type) {
        return cache.computeIfAbsent(
            type,
            t -> EnumSet.allOf((Class<Enum>) t).toArray()
        );
    }
}
