package autoparams.generator;

import java.net.URI;
import java.util.stream.Stream;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class URIStringGenerator implements ObjectGenerator {

    private static final String[] SUFFIXES = { "uri", "url" };

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query.getType().equals(String.class)
            && query instanceof ParameterQuery
            ? generate((ParameterQuery) query, context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(
        ParameterQuery query,
        ResolutionContext context
    ) {
        return query.getParameterName()
            .map(String::toLowerCase)
            .filter(name -> Stream.of(SUFFIXES).anyMatch(name::endsWith))
            .map(name -> context.resolve(URI.class).toString())
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }
}
