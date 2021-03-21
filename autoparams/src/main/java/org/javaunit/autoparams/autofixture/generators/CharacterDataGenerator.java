package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class CharacterDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.equals(char.class) || clazz.equals(Character.class);
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        byte[] bytes = new byte[1];
        RANDOM.nextBytes(bytes);
        return Optional.of((char)(bytes[0]));
    }

}
