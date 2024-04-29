package autoparams.generator;

import java.util.UUID;
import java.util.stream.Stream;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class EmailGenerator implements ObjectGenerator {

    private static final String[] SUFFIXES = { "email", "emailaddress" };

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
            .map(String::toLowerCase)
            .map(name -> name.replaceAll("_", ""))
            .filter(name -> Stream.of(SUFFIXES).anyMatch(name::endsWith))
            .map(name -> UUID.randomUUID() + "@test.com")
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }
}
