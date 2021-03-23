package org.javaunit.autoparams;

final class GenerationResult {

    private static final GenerationResult FAILURE = new GenerationResult(null);

    private final Object value;

    private GenerationResult(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    public boolean isSuccess() {
        return this != FAILURE;
    }

    public boolean isFailure() {
        return this == FAILURE;
    }

    public static GenerationResult success(Object result) {
        return new GenerationResult(result);
    }

    public static GenerationResult failure() {
        return FAILURE;
    }

}
