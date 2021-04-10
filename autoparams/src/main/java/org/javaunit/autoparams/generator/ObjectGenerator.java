package org.javaunit.autoparams.generator;

public interface ObjectGenerator {

    ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context);

    static ObjectGenerator DEFAULT = new DefaultObjectGenerator();

}
