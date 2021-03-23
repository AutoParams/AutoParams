package org.javaunit.autoparams;

import javax.annotation.Nullable;

final class GenerationResult {

    private static final GenerationResult FAILURE = new GenerationResult(null);

    @Nullable
    private final Object value;

    private GenerationResult(@Nullable Object value) {
        this.value = value;
    }

    @Nullable
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
