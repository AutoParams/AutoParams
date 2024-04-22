package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.asList;

@Deprecated
final class FreezerBuilder implements
    AnnotationConsumer<Fix>,
    AnnotationVisitor<Fix>,
    ArgumentProcessor {

    private boolean byExactType = false;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer process(Parameter parameter, Object argument) {
        return getGenerator(parameter, argument);
    }

    private ObjectGenerator getGenerator(Parameter parameter, Object argument) {
        return (query, context) -> getPredicate(parameter).test(query.getType())
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }

    private Predicate<Type> getPredicate(Parameter parameter) {
        return getPredicate(parameter.getType());
    }

    private Predicate<Type> getPredicate(Class<?> parameterType) {
        Predicate<Type> predicate = type -> false;

        if (byExactType) {
            predicate = predicate.or(type -> type.equals(parameterType));
        }

        if (byImplementedInterfaces) {
            List<Type> interfaces = asList(parameterType.getInterfaces());
            predicate = predicate.or(interfaces::contains);
        }

        return predicate;
    }

    @Override
    public void accept(Fix annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
