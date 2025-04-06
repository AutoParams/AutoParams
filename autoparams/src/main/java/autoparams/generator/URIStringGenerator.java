package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.net.URI;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static java.util.Arrays.stream;

final class URIStringGenerator implements ArgumentGenerator {

    private static final String[] SINGULAR_SUFFIXES = { "uri", "url" };
    private static final String[] PLURAL_SUFFIXES = { "uris", "urls" };

    @Override
    public ObjectContainer generate(
        ParameterQuery query,
        ResolutionContext context
    ) {
        return typeMatches(query) && nameMatches(query)
            ? new ObjectContainer(context.resolve(URI.class).toString())
            : ObjectContainer.EMPTY;
    }

    private static boolean typeMatches(ParameterQuery query) {
        return query.getType().equals(String.class);
    }

    private boolean nameMatches(ParameterQuery query) {
        return isUriParameter(query) || isPropertyOfUriContainer(query);
    }

    private boolean isUriParameter(ParameterQuery query) {
        return query.getParameterName()
            .map(String::toLowerCase)
            .filter(name -> stream(SINGULAR_SUFFIXES).anyMatch(name::endsWith))
            .isPresent();
    }

    private boolean isPropertyOfUriContainer(ParameterQuery query) {
        return stream(PLURAL_SUFFIXES).anyMatch(suffix -> {
            Parameter parameter = query.getParameter();
            Executable executable = parameter.getDeclaringExecutable();
            return executable instanceof Constructor<?>
                && RecordPredicate.test(executable.getDeclaringClass())
                && executable.getName().toLowerCase().endsWith(suffix);
        });
    }
}
