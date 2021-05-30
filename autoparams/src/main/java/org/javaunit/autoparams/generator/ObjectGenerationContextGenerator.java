package org.javaunit.autoparams.generator;

final class ObjectGenerationContextGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() == ObjectGenerationContext.class
            ? new ObjectContainer(context)
            : ObjectContainer.EMPTY;
    }

}
