package autoparams.generator;

/**
 * Exception thrown when attempting to unwrap an empty {@link ObjectContainer}.
 * <p>
 * This exception indicates that the container does not hold a value and
 * an unwrap operation was attempted, which is not allowed.
 * </p>
 *
 * @see ObjectContainer#unwrapOrElseThrow()
 */
public final class UnwrapFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    UnwrapFailedException() {
        super("The container is empty and cannot be unwrapped.");
    }
}
