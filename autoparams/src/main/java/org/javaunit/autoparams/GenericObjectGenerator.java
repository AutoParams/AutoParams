package org.javaunit.autoparams;

import java.util.Optional;

abstract class GenericObjectGenerator implements ObjectGenerator {

    @Override
    public final GenerationResult generateObject(
        ObjectQuery query,
        ObjectGenerationContext context
    ) {
        return query instanceof GenericObjectQuery
            ? generateObject((GenericObjectQuery) query, context)
            : GenerationResult.absence();
    }

    protected abstract GenerationResult generateObject(
        GenericObjectQuery query, ObjectGenerationContext context);

    @Override
    public final Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        throw new UnsupportedOperationException(MESSAGE_FOR_UNSUPPORTED_GENERATE_METHOD);
    }

}
