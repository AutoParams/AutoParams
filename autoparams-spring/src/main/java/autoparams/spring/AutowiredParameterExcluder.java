package autoparams.spring;

import java.lang.reflect.Parameter;

import autoparams.SupportedParameterPredicate;
import autoparams.customization.Decorator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.springframework.beans.factory.annotation.Autowired;

class AutowiredParameterExcluder
    extends Decorator<SupportedParameterPredicate>
    implements SupportedParameterPredicate {

    @Override
    protected SupportedParameterPredicate decorate(
        SupportedParameterPredicate component
    ) {
        return component.and(this)::test;
    }

    @Override
    public boolean test(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) {
        Parameter parameter = parameterContext.getParameter();
        return parameter.isAnnotationPresent(Autowired.class) == false;
    }
}
