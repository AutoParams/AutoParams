package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Collection;

import static java.util.Arrays.asList;

class DefaultConstructorExtractor implements ConstructorExtractor {

    @Override
    public Collection<Constructor<?>> extract(Class<?> type) {
        return asList(type.getConstructors());
    }
}
