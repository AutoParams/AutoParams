package test.autoparams.i310;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CampaignTypeFieldFreezer implements ObjectGenerator {

    private final CampaignType value;

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterQuery parameterQuery) {
        return parameterQuery
            .getParameterName()
            .map(name -> generate(name, parameterQuery.getParameter()))
            .orElse(ObjectContainer.EMPTY);
    }

    private ObjectContainer generate(String name, Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        Class<?> declaringClass = executable.getDeclaringClass();
        if (declaringClass.equals(Campaign.class) &&
            parameter.getType().equals(CampaignType.class) &&
            name.equals("type")) {
            return new ObjectContainer(value);
        } else {
            return ObjectContainer.EMPTY;
        }
    }
}
