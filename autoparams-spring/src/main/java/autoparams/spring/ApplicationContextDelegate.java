package autoparams.spring;

import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public final class ApplicationContextDelegate implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ApplicationContext applicationContext = getApplicationContext(context);
        Object bean = tryResolveBean(query.getType(), applicationContext);
        return bean == null ? ObjectContainer.EMPTY : new ObjectContainer(bean);
    }

    private static ApplicationContext getApplicationContext(
        ResolutionContext context
    ) {
        ExtensionContext extensionContext = context.getExtensionContext();
        return SpringExtension.getApplicationContext(extensionContext);
    }

    private static Object tryResolveBean(Type type, BeanFactory factory) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return factory.getBeanProvider(resolvableType).getIfAvailable();
    }
}
