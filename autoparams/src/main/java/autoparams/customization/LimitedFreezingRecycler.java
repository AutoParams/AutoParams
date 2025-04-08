package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ObjectQuery;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.internal.reflect.TypeLens;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class LimitedFreezingRecycler implements
    ArgumentRecycler,
    AnnotationConsumer<Freeze> {

    private static final Predicate<ObjectQuery> NEGATIVE = new Negative<>();

    private boolean byExactType = true;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer recycle(Object argument, Parameter parameter) {
        return getGenerator(argument, parameter);
    }

    private ObjectGenerator getGenerator(Object argument, Parameter parameter) {
        Predicate<ObjectQuery> predicate = NEGATIVE
            .or(query -> matchExactType(parameter, query))
            .or(query -> matchInterface(parameter, query));

        return (query, context) -> predicate.test(query)
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }

    private boolean matchExactType(Parameter parameter, ObjectQuery query) {
        if (byExactType == false) {
            return false;
        }

        Type parameterType = parameter.getParameterizedType();
        return query.getType().equals(parameterType);
    }

    private boolean matchInterface(Parameter parameter, ObjectQuery query) {
        if (byImplementedInterfaces == false) {
            return false;
        }

        Type parameterType = parameter.getParameterizedType();
        return new TypeLens(parameterType).implementsInterface(query.getType());
    }

    @Override
    public void accept(Freeze annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
