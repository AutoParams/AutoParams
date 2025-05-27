package autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class SealedTypeObjectGenerator implements ObjectGenerator {

    private final Random random = new Random();

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Type type = query.getType();
        return type instanceof Class<?>
            ? generate((Class<?>) type, context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(Class<?> type, ResolutionContext context) {
        if (isSealed(type) == false) {
            return ObjectContainer.EMPTY;
        }

        List<Class<?>> concreteSubclasses = findConcreteSubclasses(type);
        if (concreteSubclasses.isEmpty()) {
            return ObjectContainer.EMPTY;
        }

        Class<?> subclass = selectOne(concreteSubclasses);
        Object value = context.resolve(subclass);
        return new ObjectContainer(value);
    }

    private List<Class<?>> findConcreteSubclasses(Class<?> type) {
        List<Class<?>> result = new ArrayList<>();
        collectConcreteSubclasses(type, result);
        return result;
    }

    private void collectConcreteSubclasses(
        Class<?> type,
        List<Class<?>> result
    ) {
        for (Class<?> subclass : getPermittedSubclasses(type)) {
            if (isAbstract(subclass) == false) {
                result.add(subclass);
            } else if (isSealed(subclass)) {
                collectConcreteSubclasses(subclass, result);
            }
        }
    }

    private static boolean isAbstract(Class<?> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    private boolean isSealed(Class<?> type) {
        try {
            Method method = Class.class.getMethod("isSealed");
            return (boolean) method.invoke(type);
        } catch (Exception exception) {
            return false;
        }
    }

    private Class<?>[] getPermittedSubclasses(Class<?> type) {
        try {
            Method method = Class.class.getMethod("getPermittedSubclasses");
            return (Class<?>[]) method.invoke(type);
        } catch (Exception exception) {
            return new Class<?>[0];
        }
    }

    private Class<?> selectOne(List<Class<?>> classes) {
        return classes.get(random.nextInt(classes.size()));
    }
}
