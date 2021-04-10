package org.javaunit.autoparams;

import javax.annotation.Nullable;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.UnwrapFailedException;

final class GenerationResult {

    private static final GenerationResult ABSENCE = new GenerationResult(null);

    @Nullable
    private final Object value;

    private GenerationResult(@Nullable Object value) {
        this.value = value;
    }

    @Nullable
    public Object get() {
        return value;
    }

    public boolean isPresent() {
        return !this.isAbsent();
    }

    public boolean isAbsent() {
        return this == ABSENCE;
    }

    public static GenerationResult presence(Object value) {
        return new GenerationResult(value);
    }

    public static GenerationResult absence() {
        return ABSENCE;
    }

    public static GenerationResult fromContainer(ObjectContainer container) {
        try {
            return presence(container.unwrapOrElseThrow());
        } catch (UnwrapFailedException exception) {
            return absence();
        }
    }

}
