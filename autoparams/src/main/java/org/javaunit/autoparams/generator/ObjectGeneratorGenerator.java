package org.javaunit.autoparams.generator;

final class ObjectGeneratorGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() == ObjectGenerator.class
            ? new ObjectContainer(context.getGenerator())
            : ObjectContainer.EMPTY;
    }

}
