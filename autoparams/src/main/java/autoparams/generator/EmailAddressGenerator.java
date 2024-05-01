package autoparams.generator;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

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
            .map(name -> UUID.randomUUID() + "@" + getDomain(context))
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }

    private String getDomain(ResolutionContext context) {
        EmailAddressGenerationOptions options =
            context.resolve(EmailAddressGenerationOptions.class);
        return sample(options.domains());
    }

    private String sample(List<String> domains) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return domains.get(random.nextInt(domains.size()));
    }
}
