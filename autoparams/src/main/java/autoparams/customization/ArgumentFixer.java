package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

import static java.util.Arrays.asList;

final class ArgumentFixer implements ArgumentProcessor, AnnotationVisitor<Fix> {

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
        Predicate<Type> predicate = type -> false;

        if (byExactType) {
            predicate = predicate.or(type -> type.equals(parameter.getType()));
        }

        if (byImplementedInterfaces) {
            List<Type> interfaces = asList(parameter.getType().getInterfaces());
            predicate = predicate.or(interfaces::contains);
        }

        return predicate;
    }

    @Override
    public void visit(Fix annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
