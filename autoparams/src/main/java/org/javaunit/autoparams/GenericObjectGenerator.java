package org.javaunit.autoparams;

import java.util.Optional;

abstract class GenericObjectGenerator implements ObjectGenerator {

    @Override
    public final Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query instanceof GenericObjectQuery
            ? generate((GenericObjectQuery) query, context)
            : Optional.empty();
    }

    protected abstract Optional<Object> generate(
        GenericObjectQuery query, ObjectGenerationContext context);

}
