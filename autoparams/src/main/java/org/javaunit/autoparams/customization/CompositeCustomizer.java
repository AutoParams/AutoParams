package org.javaunit.autoparams.customization;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.javaunit.autoparams.generator.ObjectGenerator;

public class CompositeCustomizer implements Customizer {

    private final Customizer[] customizers;

    public CompositeCustomizer(Customizer... customizers) {
        this.customizers = customizers;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return foldl(Arrays.stream(customizers), generator, (g, c) -> c.customize(g));
    }

    private static <T, U> U foldl(Stream<T> xs, U z, BiFunction<U, T, U> f) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }

}
