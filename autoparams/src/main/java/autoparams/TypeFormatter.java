package autoparams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class TypeFormatter {

    static String format(Type type, boolean verbose) {
        if (type instanceof Class<?>) {
            return verbose
                ? ((Class<?>) type).getName()
                : ((Class<?>) type).getSimpleName();
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            String rawTypeName = format(rawType, verbose);

            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            StringBuilder sb = new StringBuilder(rawTypeName);
            sb.append("<");
            for (int i = 0; i < typeArguments.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(format(typeArguments[i], verbose));
            }
            sb.append(">");
            return sb.toString();
        } else {
            return type.getTypeName();
        }
    }
}
