package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static java.util.Arrays.stream;

final class URIStringGenerator implements ObjectGenerator {

    private static final String[] SINGULAR_SUFFIXES = { "uri", "url" };
    private static final String[] PLURAL_SUFFIXES = { "uris", "urls" };

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
        return isUriParameter(query) || isPropertyOfUriContainer(query)
            ? new ObjectContainer(context.resolve(URI.class).toString())
            : ObjectContainer.EMPTY;
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
            return isRecordConstructor(executable)
                && executable.getName().toLowerCase().endsWith(suffix);
        });
    }

    private boolean isRecordConstructor(Executable executable) {
        return executable instanceof Constructor<?>
            && isRecord(executable.getDeclaringClass());
    }

    private boolean isRecord(Class<?> type) {
        try {
            Method predicate = Class.class.getMethod("isRecord");
            try {
                return (boolean) predicate.invoke(type);
            } catch (IllegalAccessException |
                     InvocationTargetException exception) {
                throw new RuntimeException(exception);
            }
        } catch (NoSuchMethodException exception) {
            return false;
        }
    }
}
