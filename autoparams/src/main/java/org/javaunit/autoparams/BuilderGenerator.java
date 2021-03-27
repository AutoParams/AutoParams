package org.javaunit.autoparams;

final class BuilderGenerator extends GenericObjectGenerator {

    @Override
    protected GenerationResult generate(GenericObjectQuery query, ObjectGenerationContext context) {
        return query.getType() == Builder.class
            ? GenerationResult.presence(new Builder<>(query, context))
            : GenerationResult.absence();
    }

}
