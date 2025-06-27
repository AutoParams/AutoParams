package autoparams;

import java.lang.reflect.Type;

class TypeFormatter {

    static String format(Type type, boolean verbose) {
        if (type instanceof Class<?>) {
            return verbose
                ? ((Class<?>) type).getName()
                : ((Class<?>) type).getSimpleName();
        } else {
            return type.getTypeName();
        }
    }
}
