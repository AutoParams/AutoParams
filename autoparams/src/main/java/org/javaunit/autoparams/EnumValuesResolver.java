package org.javaunit.autoparams;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class EnumValuesResolver {

    private static final Map<Class<?>, Object[]> CACHE = new ConcurrentHashMap<>();

    public static Object[] resolveValues(Class<?> type) {
        return CACHE.computeIfAbsent(type, EnumValuesResolver::getValues);
    }

    private static Object[] getValues(Class<?> type) {
        try {
            return (Object[]) type.getDeclaredMethod("values").invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
