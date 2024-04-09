package autoparams;

import java.lang.reflect.Constructor;

final class Instantiator {

    public static <T> T instantiate(Class<? extends T> type) {
        try {
            Constructor<? extends T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
