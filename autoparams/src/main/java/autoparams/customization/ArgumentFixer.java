package autoparams.customization;

import autoparams.generator.ObjectContainer;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

final class ArgumentFixer implements ArgumentProcessor, AnnotationVisitor<Fix> {

    private boolean byExactType = false;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer process(Parameter parameter, Object argument) {
        List<Function<Type, Boolean>> predicates = new ArrayList<>();

        if (byExactType) {
            predicates.add(type -> type.equals(parameter.getType()));
        }

        if (byImplementedInterfaces) {
            Type[] interfaces = parameter.getType().getInterfaces();
            predicates.add(type -> Arrays.asList(interfaces).contains(type));
        }

        return generator -> (query, context) ->
            predicates.stream().anyMatch(predicate -> predicate.apply(query.getType()))
                ? new ObjectContainer(argument)
                : generator.generate(query, context);
    }

    @Override
    public void visit(Fix annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
