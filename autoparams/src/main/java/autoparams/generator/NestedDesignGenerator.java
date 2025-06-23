package autoparams.generator;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.dsl.FunctionDelegate;

final class NestedDesignGenerator<T, P> implements ArgumentGenerator {

    private final Predicate<ParameterQuery> predicate;
    private final DesignLanguage<P> designLanguage;

    NestedDesignGenerator(FunctionDelegate<T, P> getter, DesignLanguage<P> designLanguage) {
        this.predicate = createParameterPredicate(getter);
        this.designLanguage = designLanguage;
    }

    private Predicate<ParameterQuery> createParameterPredicate(FunctionDelegate<T, P> getter) {
        // ArgumentCustomizationDsl.set() 메서드와 같은 로직을 복제
        try {
            Method getterMethod = extractGetterMethod(getter);
            String expectedParameterName = inferParameterNameFromGetter(getterMethod);
            Class<?> expectedParameterType = getterMethod.getReturnType();

            return query -> {
                try {
                    String actualParameterName = query.getRequiredParameterName();
                    Class<?> actualParameterType = (Class<?>) query.getType();

                    return expectedParameterName.equals(actualParameterName) &&
                           expectedParameterType.equals(actualParameterType);
                } catch (Exception e) {
                    return false;
                }
            };
        } catch (Exception e) {
            return query -> false;
        }
    }

    // GetterDelegate.getGetterOf() 로직을 복제
    private Method extractGetterMethod(FunctionDelegate<T, P> delegate) {
        SerializedLambda lambda = getLambda(delegate);
        return isInKotlin(lambda) ? getKotlinGetter(lambda) : getJavaGetter(lambda);
    }

    private SerializedLambda getLambda(FunctionDelegate<T, P> delegate) {
        try {
            Method writeReplace = delegate.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(delegate);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private boolean isInKotlin(SerializedLambda lambda) {
        return lambda.getCapturedArgCount() == 1;
    }

    private Method getKotlinGetter(SerializedLambda lambda) {
        Object arg = lambda.getCapturedArg(0);
        Object owner = invokeGetter(arg, "getOwner");
        Class<?> componentType = invokeGetter(owner, "getJClass");
        String signature = invokeGetter(arg, "getSignature");
        String getterName = signature.substring(0, signature.indexOf('('));
        return getGetter(componentType, getterName);
    }

    @SuppressWarnings("unchecked")
    private <U> U invokeGetter(Object instance, String getterName) {
        try {
            Method getter = instance.getClass().getMethod(getterName);
            return (U) getter.invoke(instance);
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Method getJavaGetter(SerializedLambda lambda) {
        try {
            String componentTypeName = lambda.getImplClass().replace('/', '.');
            Class<?> componentType = Class.forName(componentTypeName);
            return getGetter(componentType, lambda.getImplMethodName());
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Method getGetter(Class<?> type, String getterName) {
        try {
            return type.getMethod(getterName);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    // ParameterNameInferencer.inferParameterNameFromGetter() 로직을 복제
    private String inferParameterNameFromGetter(Method getter) {
        return decapitalizeHead(removeGetterPrefix(getter.getName()));
    }

    private String removeGetterPrefix(String getterName) {
        if (hasIsPrefix(getterName)) {
            return getterName.substring(2);
        } else if (hasGetPrefix(getterName)) {
            return getterName.substring(3);
        } else {
            return getterName;
        }
    }

    private boolean hasIsPrefix(String methodName) {
        return methodName.startsWith("is") && methodName.length() > 2
            && Character.isUpperCase(methodName.charAt(2));
    }

    private boolean hasGetPrefix(String methodName) {
        return methodName.startsWith("get") && methodName.length() > 3
            && Character.isUpperCase(methodName.charAt(3));
    }

    private String decapitalizeHead(String s) {
        char head = s.charAt(0);
        return Character.isUpperCase(head) ? Character.toLowerCase(head) + s.substring(1) : s;
    }

    @Override
    public ObjectContainer generate(ParameterQuery query, ResolutionContext context) {
        if (!predicate.test(query)) {
            return ObjectContainer.EMPTY;
        }

        // Create a factory for the nested object type and apply the design
        try {
            Class<?> nestedType = (Class<?>) query.getType();
            Factory<?> factory = Factory.create(nestedType);
            Object nestedObject = factory.get(designLanguage.getGenerators());
            return new ObjectContainer(nestedObject);
        } catch (Exception e) {
            return ObjectContainer.EMPTY;
        }
    }
}
