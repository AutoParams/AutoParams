package autoparams.customization.dsl;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

class DeclaringClassEquals implements Predicate<ParameterQuery> {

    private final Class<?> declaringClass;

    public DeclaringClassEquals(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
    }

    @Override
    public boolean test(ParameterQuery query) {
        Parameter parameter = query.getParameter();
        Executable executable = parameter.getDeclaringExecutable();
        return executable.getDeclaringClass().equals(declaringClass);
    }
}
