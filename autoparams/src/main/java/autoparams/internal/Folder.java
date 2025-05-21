package autoparams.internal;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class Folder {

    private Folder() {
    }

    public static <T, U> U foldl(BiFunction<U, T, U> f, U z, Stream<T> xs) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }
}
