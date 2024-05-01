package autoparams.generator;

import java.util.UUID;
import java.util.stream.Stream;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.Sampling.sample;

final class EmailAddressGenerator implements ObjectGenerator {

    private static final String[] SUFFIXES = { "email", "emailaddress" };

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
            .map(name -> name.replaceAll("_", ""))
            .filter(name -> Stream.of(SUFFIXES).anyMatch(name::endsWith))
            .map(name -> generateEmailAddress(context))
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }

    private String generateEmailAddress(ResolutionContext context) {
        EmailAddressGenerationOptions options = getOptions(context);
        return UUID.randomUUID() + "@" + getDomain(options);
    }

    private static EmailAddressGenerationOptions getOptions(
        ResolutionContext context
    ) {
        return context.resolve(EmailAddressGenerationOptions.class);
    }

    private String getDomain(EmailAddressGenerationOptions options) {
        return sample(options.domains());
    }
}
