package org.javaunit.autoparams;

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

}
