package org.javaunit.autoparams.generator;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class EnumValuesResolver {

    private static final Map<Class<?>, Object[]> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object[] resolveValues(Class<?> type) {
        return CACHE.computeIfAbsent(type, it -> EnumSet.allOf((Class<Enum>) it).toArray());
    }
}
