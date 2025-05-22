package autoparams.spring;

import java.lang.reflect.Type;
import java.util.function.Function;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ResolvableType;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

/**
 * An {@link ObjectGenerator} that resolves requested objects from a Spring
 * {@link BeanFactory}.
 * <p>
 * This generator attempts to retrieve an instance of the requested type from
 * the Spring application context. If the requested type is a registered Spring
 * bean, an instance of that bean is returned. If the type is not found in the
 * Spring context, this generator yields an empty result, allowing other
 * generators in the chain to attempt to create the object.
 * </p>
 * <p>
 * This generator is typically used in conjunction with Spring testing utilities
 * to integrate AutoParams with the Spring dependency injection mechanism.
 * </p>
 *
 * @see ObjectGenerator
 * @see BeanFactory
 * @see org.springframework.context.ApplicationContext
 * @see org.springframework.test.context.junit.jupiter.SpringExtension
 * @see UseBeans
 */
public final class BeanGenerator implements ObjectGenerator {

    private record ConstantFunction<T, R>(R value) implements Function<T, R> {

        @Override
        public R apply(T t) {
            return value;
        }
    }

    private final Function<ResolutionContext, BeanFactory> beanFactoryResolver;

    private BeanGenerator(
        Function<ResolutionContext, BeanFactory> beanFactoryResolver
    ) {
        this.beanFactoryResolver = beanFactoryResolver;
    }

    /**
     * Creates an instance of {@link BeanGenerator} with a specific
     * {@link BeanFactory}.
     * <p>
     * This constructor allows providing a concrete {@link BeanFactory} that
     * will be used to resolve bean instances. This is useful when the
     * {@link BeanFactory} is already available or when you want to use a
     * specific factory for testing purposes.
     * </p>
     *
     * @param beanFactory The Spring {@link BeanFactory} to be used for
     *                    resolving beans.
     */
    public BeanGenerator(BeanFactory beanFactory) {
        this(new ConstantFunction<>(beanFactory));
    }

    /**
     * Creates an instance of {@link BeanGenerator} that dynamically resolves
     * the {@link BeanFactory} using the
     * {@link org.springframework.test.context.junit.jupiter.SpringExtension SpringExtension}.
     * <p>
     * This constructor is typically used when integrating with Spring tests
     * managed by the Spring TestContext Framework.
     * </p>
     * <p>
     * This approach allows {@link BeanGenerator} to seamlessly access beans
     * from the Spring container active during the test.
     * </p>
     */
    public BeanGenerator() {
        this(BeanGenerator::resolveBeanFactory);
    }

    private static BeanFactory resolveBeanFactory(ResolutionContext context) {
        return getApplicationContext(context.resolve(ExtensionContext.class));
    }

    /**
     * Generates an object based on the provided query and resolution context.
     * <p>
     * This method attempts to resolve a Spring-managed bean that matches the
     * type specified in the {@link ObjectQuery}. It uses the configured
     * {@link BeanFactory} to look up the bean.
     * </p>
     * <p>
     * If a bean matching the requested type is found in the Spring application
     * context, an {@link ObjectContainer} containing the bean instance is
     * returned. Otherwise, {@link ObjectContainer#EMPTY} is returned.
     * </p>
     *
     * @param query   The {@link ObjectQuery} specifying the type of object
     *                requested.
     * @param context The {@link ResolutionContext} providing context for object
     *                generation.
     * @return An {@link ObjectContainer} containing the resolved bean if found;
     *         otherwise, {@link ObjectContainer#EMPTY}.
     */
    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return generate(context, query.getType());
    }

    private ObjectContainer generate(ResolutionContext context, Type type) {
        if (type.equals(ExtensionContext.class)) {
            return ObjectContainer.EMPTY;
        }

        BeanFactory factory = beanFactoryResolver.apply(context);
        Object bean = getBeanIfAvailable(factory, type);
        return bean == null ? ObjectContainer.EMPTY : new ObjectContainer(bean);
    }

    private static Object getBeanIfAvailable(BeanFactory factory, Type type) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return factory.getBeanProvider(resolvableType).getIfAvailable();
    }
}
