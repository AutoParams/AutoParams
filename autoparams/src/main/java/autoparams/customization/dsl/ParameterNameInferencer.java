package autoparams.customization.dsl;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

class ParameterNameInferencer {

    public static String inferParameterName(String getterName) {
        return decapitalizeFirstCharacter(removePrefix(getterName));
    }

    private static String removePrefix(String getterName) {
        if (hasIsPrefix(getterName)) {
            return getterName.substring(2);
        } else if (hasGetPrefix(getterName)) {
            return getterName.substring(3);
        } else {
            return getterName;
        }
    }

    private static boolean hasIsPrefix(String getterName) {
        return getterName.startsWith("is")
            && getterName.length() > 2
            && isUpperCase(getterName.charAt(2));
    }

    private static boolean hasGetPrefix(String getterName) {
        return getterName.startsWith("get")
            && getterName.length() > 3
            && isUpperCase(getterName.charAt(3));
    }

    private static String decapitalizeFirstCharacter(String name) {
        return isUpperCase(name.charAt(0))
            ? toLowerCase(name.charAt(0)) + name.substring(1)
            : name;
    }
}
