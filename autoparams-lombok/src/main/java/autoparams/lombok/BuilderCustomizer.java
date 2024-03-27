package autoparams.lombok;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;

public class BuilderCustomizer implements Customizer {

    private final String builderMethodName;
    private final String buildMethodName;

    public BuilderCustomizer() {
        this("builder", "build");
    }

    protected BuilderCustomizer(String builderMethodName, String buildMethodName) {
        this.builderMethodName = builderMethodName;
        this.buildMethodName = buildMethodName;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> getBuilder(query.getType())
            .map(builder -> factory(builder, context))
            .orElseGet(() -> generator.generate(query, context));
    }

    private Optional<Object> getBuilder(Type type) {
        return type instanceof Class<?>
            ? getBuilder((Class<?>) type)
            : Optional.empty();
    }

    private Optional<Object> getBuilder(Class<?> type) {
        return Arrays
            .stream(type.getDeclaredMethods())
            .filter(method -> method.getName().equals(builderMethodName))
            .filter(method -> Modifier.isStatic(method.getModifiers()))
            .map(method -> invoke(null, method))
            .findFirst();
    }

    private ObjectContainer factory(Object builder, ObjectGenerationContext context) {
        setProperties(builder, context);
        return buildObject(builder);
    }

    private static void setProperties(Object builder, ObjectGenerationContext context) {
        Arrays
            .stream(builder.getClass().getDeclaredMethods())
            .filter(method -> method.getParameterCount() == 1)
            .forEach(setter -> setProperty(builder, setter, context));
    }

    private static void setProperty(
        Object builder,
        Method setter,
        ObjectGenerationContext context
    ) {
        ObjectQuery query = () -> setter.getGenericParameterTypes()[0];
        Object argument = context.generate(query);
        invoke(builder, setter, argument);
    }

    private ObjectContainer buildObject(Object builder) {
        try {
            Method build = builder.getClass().getMethod(buildMethodName);
            return new ObjectContainer(invoke(builder, build));
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

