package org.javaunit.autoparams.autofixture.generators;

import org.javaunit.autoparams.autofixture.BasicSupportGenerators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class ObjectDataGenerator implements DataGenerator {
    // this method always return true because this generator is last DataGenerator
    @Override
    public boolean isSupport(Class clazz) {
        return true;
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];
        try {
            constructor.setAccessible(true);
        } catch (SecurityException e) {
            System.out.println(e);
        }
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int parameterSize = parameterTypes.length;
        Object[] parameters = new Object[parameterSize];

        // constructors 0 is not argument(constructor's class)... constructor args start with 1
        for(int i=1; i< parameterSize; i++) {
            Class<?> c = parameterTypes[i];
            parameters[i] = BasicSupportGenerators.dataGenerators.stream()
                .filter(x -> x.isSupport(c))
                .map(x -> x.generate(c, parameterizedType))
                .map(x -> x.orElse(null))
                .findFirst()
                .orElse(null);
        }

        try {
            constructor.setAccessible(true);
            Object newInstance = constructor.newInstance(parameters);
            // field array's last index  is not argument... constructor args end index is last index -1
            Field[] fields = newInstance.getClass().getDeclaredFields();
            for(int i=0; i < fields.length -1; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                Class c = f.getType();
                ParameterizedType tempParameterizedType = null;
                try {
                    tempParameterizedType = (ParameterizedType) f.getGenericType();
                } catch (ClassCastException e) {
                    // ignore
                }
                ParameterizedType pt = tempParameterizedType;
                Optional<Object> optional = BasicSupportGenerators.dataGenerators.stream()
                    .filter(x -> x.isSupport(c))
                    .map(x -> x.generate(c, pt))
                    .findFirst()
                    .orElse(null);
                Object v = optional.orElse(null);
                f.set(newInstance, v);
            }
            return Optional.of(newInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
