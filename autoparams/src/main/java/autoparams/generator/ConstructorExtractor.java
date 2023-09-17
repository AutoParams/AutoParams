package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Collection;

@FunctionalInterface
public interface ConstructorExtractor {

    Collection<Constructor<?>> extract(Class<?> type);
}
