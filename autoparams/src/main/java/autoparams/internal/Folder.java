package autoparams.internal;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Provides a utility method for folding operations for streams.
 * <p>
 * This class is not meant to be instantiated and contains only static utility
 * methods.
 * </p>
 */
public final class Folder {

    private Folder() {
    }

    /**
     * Performs a left-associative fold operation on a stream.
     * <p>
     * This method applies a binary operator to an initial value and all
     * elements of the stream, going from left to right. For a stream
     * {@code [x1, x2, ..., xn]}, the result is
     * {@code f(...f(f(z, x1), x2)..., xn)}.
     * </p>
     *
     * @param <T> the type of the elements in the stream.
     * @param <U> the type of the result and the initial value.
     * @param f   the binary operator to apply. This function takes the
     *            current accumulated value and the next element from
     *            the stream as arguments and returns the new accumulated
     *            value.
     * @param z   the initial value for the fold operation.
     * @param xs  the stream of elements to fold.
     * @return the result of the left fold operation.
     * @see Stream
     * @see BiFunction
     */
    public static <T, U> U foldl(BiFunction<U, T, U> f, U z, Stream<T> xs) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }
}
