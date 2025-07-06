package autoparams.internal.reflect;

import java.beans.PropertyDescriptor;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import autoparams.customization.dsl.FunctionDelegate;

public class PropertyReflector {

    public static <T, R> PropertyDescriptor getProperty(
        FunctionDelegate<T, R> getterDelegate
    ) {
        Method getter = getGetterFrom(getterDelegate);
        return getPropertyDescriptor(getter);
    }

    private static <T, R> Method getGetterFrom(FunctionDelegate<T, R> delegate) {
        SerializedLambda lambda = getLambda(delegate);
        return isInKotlin(lambda)
            ? getKotlinGetter(lambda)
            : getJavaGetter(lambda);
    }

    private static <T, R> SerializedLambda getLambda(
        FunctionDelegate<T, R> delegate
    ) {
        try {
            Method writeReplace = delegate
                .getClass()
                .getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(delegate);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static boolean isInKotlin(SerializedLambda lambda) {
        return lambda.getCapturedArgCount() == 1;
    }

    private static Method getKotlinGetter(SerializedLambda lambda) {
        Object arg = lambda.getCapturedArg(0);
        Object owner = invokeGetter(arg, "getOwner");
        Class<?> componentType = invokeGetter(owner, "getJClass");
        String signature = invokeGetter(arg, "getSignature");
        String getterName = signature.substring(0, signature.indexOf('('));
        return getGetter(componentType, getterName);
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeGetter(Object instance, String getterName) {
        try {
            Method getter = instance.getClass().getMethod(getterName);
            return (T) getter.invoke(instance);
        } catch (NoSuchMethodException |
                 IllegalAccessException |
                 InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Method getJavaGetter(SerializedLambda lambda) {
        try {
            String componentTypeName = lambda.getImplClass().replace('/', '.');
            Class<?> componentType = Class.forName(componentTypeName);
            return getGetter(componentType, lambda.getImplMethodName());
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Method getGetter(Class<?> type, String getterName) {
        try {
            return type.getMethod(getterName);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static PropertyDescriptor getPropertyDescriptor(Method getter) {
        String propertyName = inferParameterNameFromGetter(getter);
        try {
            return new PropertyDescriptor(propertyName, getter, null);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String inferParameterNameFromGetter(Method getter) {
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
            && Character.isUpperCase(methodName.charAt(2));
    }

    private static boolean hasGetPrefix(String methodName) {
        return methodName.startsWith("get")
            && methodName.length() > 3
            && Character.isUpperCase(methodName.charAt(3));
    }

    private static String decapitalizeHead(String s) {
        char head = s.charAt(0);
        return Character.isUpperCase(head) ? Character.toLowerCase(head) + s.substring(1) : s;
    }
}
