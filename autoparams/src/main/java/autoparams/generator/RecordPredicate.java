package autoparams.generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

final class RecordPredicate {

    private static final ConcurrentHashMap<Class<?>, Boolean> CACHE;
    private static final Predicate<Class<?>> NEGATIVE;

    private static Predicate<Class<?>> predicate;

    static {
        CACHE = new ConcurrentHashMap<>();
        NEGATIVE = x -> false;
        predicate = null;
    }

    public static boolean test(Class<?> type) {
        return CACHE.computeIfAbsent(type, RecordPredicate::invokePredicate);
    }

    private static boolean invokePredicate(Class<?> type) {
        ensurePredicate();
        return predicate.test(type);
    }

    private static void ensurePredicate() {
        if (predicate == null) {
            setPredicate();
        }
    }

    private static void setPredicate() {
        try {
            predicate = new Delegate(Class.class.getMethod("isRecord"));
        } catch (NoSuchMethodException exception) {
            predicate = NEGATIVE;
        }
    }

    private static class Delegate implements Predicate<Class<?>> {

        private final Method method;

        public Delegate(Method method) {
            this.method = method;
        }

        public boolean test(Class<?> type) {
            try {
                return (boolean) method.invoke(type);
            } catch (IllegalAccessException |
                     InvocationTargetException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
