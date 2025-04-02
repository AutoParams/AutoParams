package autoparams.generator;

import java.util.UUID;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class StringGenerator extends ObjectGeneratorBase<String> {

    @Override
    protected String generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query)
            : generate();
    }

    private String generate(ParameterQuery query) {
        return query
            .getParameterName()
            .map(name -> name + generate())
            .orElseGet(this::generate);
    }

    private String generate() {
        return UUID.randomUUID().toString();
    }
}
