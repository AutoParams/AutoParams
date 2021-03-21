package org.javaunit.autoparams;

import java.util.Optional;

final class BuilderGenerator extends GenericObjectGenerator {

    @Override
    protected Optional<Object> generate(GenericObjectQuery query, ObjectGenerationContext context) {
        return query.getType() == Builder.class
            ? Optional.of(new Builder<>(query, context))
            : Optional.empty();
    }
}
