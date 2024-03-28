package autoparams.customization;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

import static java.util.Arrays.stream;

public class CompositeCustomizer implements Customizer {

    private final Customizer[] customizers;

    public CompositeCustomizer(Customizer... customizers) {
        this.customizers = customizers;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return foldl((g, c) -> c.customize(g), generator, stream(customizers));
    }

    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        return foldl((p, c) -> c.customize(p), processor, stream(customizers));
    }

    private static <T, U> U foldl(BiFunction<U, T, U> f, U z, Stream<T> xs) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }
}
