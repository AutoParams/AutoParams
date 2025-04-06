package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.UUID;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.Sampling.sample;
import static java.util.Arrays.stream;

final class EmailAddressStringGenerator implements ArgumentGenerator {

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
        ParameterQuery query,
        ResolutionContext context
    ) {
        return typeMatches(query) && nameMatches(query)
            ? generateEmailAddress(context)
            : ObjectContainer.EMPTY;
    }

    private static boolean typeMatches(ParameterQuery query) {
        return query.getType().equals(String.class);
    }

    private boolean nameMatches(ParameterQuery query) {
        return isEmailAddressParameter(query)
            || isPropertyOfEmailAddressContainer(query);
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
