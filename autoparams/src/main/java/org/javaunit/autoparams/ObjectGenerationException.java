package org.javaunit.autoparams;

public class ObjectGenerationException extends RuntimeException {
    public ObjectGenerationException(String message) {
        super(message);
    }

    public ObjectGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
