package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.javaunit.autoparams.generator.ObjectQuery;

final class BuilderGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType())
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type) {
        return type.getRawType().equals(Builder.class)
            ? new ObjectContainer(factory(type.getActualTypeArguments()[0]))
            : ObjectContainer.EMPTY;
    }

    private Builder<?> factory(Type targetType) {
        return Builder.create(targetType, ObjectGenerator.DEFAULT);
    }

}
