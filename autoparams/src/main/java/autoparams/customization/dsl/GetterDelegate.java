package autoparams.customization.dsl;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GetterDelegate {

    public static <T, R> Method getGetterOf(FunctionDelegate<T, R> delegate) {
        SerializedLambda lambda = getLambda(delegate);
        return isInKotlin(lambda)
            ? getKotlinGetter(lambda)
            : getJavaGetter(lambda);
    }

    public static <T, R> SerializedLambda getLambda(
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
}
