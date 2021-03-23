package org.javaunit.autoparams;

import javax.annotation.Nullable;

final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;

    }

    @Nullable
    public Object generate(ObjectQuery query) {
        GenerationResult result = generator.generateObject(query, this);
        if (result.isFailure()) {
            String format = "An object cannot be generated with the given query '%s'. "
                + "This can happen if the query represents an interface or abstract class.";

            throw new ObjectGenerationException(String.format(format, query.getType()));
        }

        return result.get();
    }

}
