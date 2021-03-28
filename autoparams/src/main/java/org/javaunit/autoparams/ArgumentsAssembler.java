package org.javaunit.autoparams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

final class ArgumentsAssembler {

    public static Stream<? extends Arguments> assembleArguments(
        ExtensionContext context,
        ArgumentsProvider... providers
    ) {
        return foldl(
            (sets, provider) -> sets.flatMap(set -> supplementArguments(set, provider, context)),
            Stream.of(Arguments.of()),
            Arrays.stream(providers));
    }

    private static <T, U> U foldl(BiFunction<U, T, U> f, U z, Stream<T> xs) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }

    private static Stream<? extends Arguments> supplementArguments(
        Arguments source,
        ArgumentsProvider provider,
        ExtensionContext context
    ) {
        try {
            return provider
                .provideArguments(context)
                .map(supplement -> supplementArguments(source, supplement));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Arguments supplementArguments(Arguments source, Arguments supplement) {
        ArrayList<Object> arguments = new ArrayList<Object>();
        Collections.addAll(arguments, source.get());
        Arrays.stream(supplement.get()).skip(arguments.size()).forEach(arguments::add);
        return Arguments.of(arguments.toArray());
    }

}
