package org.javaunit.autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.javaunit.autoparams.generator.ObjectContainer;

final class ArgumentFixer implements ArgumentProcessor, AnnotationVisitor<Fix> {

    private boolean byExactType = false;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer process(Parameter parameter, Object argument) {
        List<Function<Type, Boolean>> predicates = new ArrayList<Function<Type, Boolean>>();

        if (byExactType) {
            predicates.add(type -> type.equals(parameter.getType()));
        }

        if (byImplementedInterfaces) {
            Class<?>[] interfaces = parameter.getType().getInterfaces();
            predicates.add(type -> Arrays.stream(interfaces).anyMatch(type::equals));
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
