package autoparams;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.extension.ExtensionContext;

final class BuilderGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return type.getRawType().equals(Builder.class)
            ? new ObjectContainer(factory(type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private Builder<?> factory(Type targetType, ObjectGenerationContext context) {
        return Builder.create(
            targetType,
            new ObjectGenerationContext(
                context.generate(ExtensionContext.class),
                ObjectGenerator.DEFAULT
            )
        );
    }

}
