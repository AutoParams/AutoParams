package autoparams.customization.dsl;

import java.lang.reflect.Method;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

class ParameterNameInferencer {

    public static String inferParameterNameFromGetter(Method getter) {
        return decapitalizeHead(removeGetterPrefix(getter.getName()));
    }

    private static String removeGetterPrefix(String getterName) {
        if (hasIsPrefix(getterName)) {
            return getterName.substring(2);
        } else if (hasGetPrefix(getterName)) {
            return getterName.substring(3);
        } else {
            return getterName;
        }
    }

    private static boolean hasIsPrefix(String methodName) {
        return methodName.startsWith("is")
            && methodName.length() > 2
            && isUpperCase(methodName.charAt(2));
    }

    private static boolean hasGetPrefix(String methodName) {
        return methodName.startsWith("get")
            && methodName.length() > 3
            && isUpperCase(methodName.charAt(3));
    }

    private static String decapitalizeHead(String s) {
        char head = s.charAt(0);
        return isUpperCase(head) ? toLowerCase(head) + s.substring(1) : s;
    }
}
