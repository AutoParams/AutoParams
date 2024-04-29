package autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;

final class EmailGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query.getType().equals(String.class)
            && query instanceof ParameterQuery
            ? generate((ParameterQuery) query)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterQuery query) {
        return query.getParameterName()
            .filter(name -> name.toLowerCase().endsWith("email"))
            .map(name -> UUID.randomUUID() + "@test.com")
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }
}
