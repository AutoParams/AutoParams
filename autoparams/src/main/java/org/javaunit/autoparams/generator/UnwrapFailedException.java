package org.javaunit.autoparams.generator;

public final class UnwrapFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    UnwrapFailedException() {
        super("The container is empty and cannot be unwrapped.");
    }

}
