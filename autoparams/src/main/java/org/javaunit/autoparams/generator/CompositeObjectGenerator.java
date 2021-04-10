package org.javaunit.autoparams.generator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class CompositeObjectGenerator implements ObjectGenerator {

    private ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return foldl(
            (result, generator) -> result.yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            Arrays.stream(generators)
        );
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
