package org.javaunit.autoparams;

abstract class GenericObjectGenerator implements ObjectGenerator {

    @Override
    public final GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
        return query instanceof GenericObjectQuery
            ? generate((GenericObjectQuery) query, context)
            : GenerationResult.absence();
    }

    protected abstract GenerationResult generate(
        GenericObjectQuery query, ObjectGenerationContext context);

}
