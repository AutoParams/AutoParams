package autoparams.lombok;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Optional;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

import static java.util.Arrays.stream;

class BuilderInvoker implements ObjectGenerator {

    private final String builderMethodName;
    private final String buildMethodName;

    public BuilderInvoker(String builderMethodName, String buildMethodName) {
        this.builderMethodName = builderMethodName;
        this.buildMethodName = buildMethodName;
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return getBuilder(query.getType())
            .map(builder -> factory(builder, context))
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }

    private Optional<Object> getBuilder(Type type) {
        return type instanceof Class<?>
            ? getBuilder((Class<?>) type)
            : Optional.empty();
    }

    private Optional<Object> getBuilder(Class<?> type) {
        return stream(type.getDeclaredMethods())
            .filter(method -> method.getName().equals(builderMethodName))
            .filter(method -> Modifier.isStatic(method.getModifiers()))
            .map(method -> invoke(null, method))
            .findFirst();
    }

    private Object factory(Object builder, ResolutionContext context) {
        setProperties(builder, context);
        return buildObject(builder);
    }

    private static void setProperties(
        Object builder,
        ResolutionContext context
    ) {
        stream(builder.getClass().getDeclaredMethods())
            .filter(method -> method.getParameterCount() == 1)
            .forEach(setter -> setProperty(builder, setter, context));
    }

    private static void setProperty(
        Object builder,
        Method setter,
        ResolutionContext context
    ) {
        ParameterQuery query = new ParameterQuery(
            setter.getParameters()[0],
            0,
            setter.getGenericParameterTypes()[0]
        );
        Object argument = context.resolve(query);
        invoke(builder, setter, argument);
    }

    private Object buildObject(Object builder) {
        try {
            Method build = builder.getClass().getMethod(buildMethodName);
            return invoke(builder, build);
        } catch (NoSuchMethodException |
                 SecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Object invoke(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }
}
