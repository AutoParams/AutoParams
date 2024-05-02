package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.UUID;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.Sampling.sample;
import static java.util.Arrays.stream;

final class EmailAddressStringGenerator implements ObjectGenerator {

    private static final String[] SINGULAR_SUFFIXES = {
        "email",
        "emailaddress",
        "email_address"
    };

    private static final String[] PLURAL_SUFFIXES = {
        "emails",
        "emailaddresses",
        "email_addresses"
    };

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
        return isEmailAddressParameter(query)
            || isPropertyOfEmailAddressContainer(query)
            ? generateEmailAddress(context)
            : ObjectContainer.EMPTY;
    }

    private boolean isEmailAddressParameter(ParameterQuery query) {
        return query.getParameterName()
            .map(String::toLowerCase)
            .filter(name -> stream(SINGULAR_SUFFIXES).anyMatch(name::endsWith))
            .isPresent();
    }

    private boolean isPropertyOfEmailAddressContainer(ParameterQuery query) {
        return stream(PLURAL_SUFFIXES).anyMatch(suffix -> {
            Parameter parameter = query.getParameter();
            Executable executable = parameter.getDeclaringExecutable();
            return executable instanceof Constructor<?>
                && RecordPredicate.test(executable.getDeclaringClass())
                && executable.getName().toLowerCase().endsWith(suffix);
        });
    }

    private ObjectContainer generateEmailAddress(ResolutionContext context) {
        EmailAddressGenerationOptions options = getOptions(context);
        String localPart = UUID.randomUUID().toString();
        String domainPart = getDomain(options);
        return new ObjectContainer(localPart + "@" + domainPart);
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
