package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import autoparams.AnnotationScanner.Edge;
import autoparams.customization.AnnotatedElementConsumer;
import autoparams.customization.ArgumentRecycler;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.customization.RecycleArgument;
import lombok.val;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Brake.collectBrakes;
import static autoparams.Instantiator.instantiate;

class TestResolutionContext extends ResolutionContext {

    private static final Object[] EMPTY_ASSET = new Object[0];

    static TestResolutionContext create(ExtensionContext extensionContext) {
        val resolutionContext = new TestResolutionContext();

        Method method = extensionContext.getRequiredTestMethod();
        if (method.isAnnotationPresent(LogResolution.class)) {
            resolutionContext.enableLogging();
        }

        resolutionContext.initialize(extensionContext);
        return resolutionContext;
    }

    private void initialize(ExtensionContext extensionContext) {
        applyCustomizer(new ExtensionContextProvider(extensionContext));
        applyAnnotatedCustomizers(extensionContext.getRequiredTestMethod());
    }

    private void applyAnnotatedCustomizers(AnnotatedElement element) {
        for (Edge<Annotation> edge : scanAnnotations(element)) {
            if (edge.getCurrent() instanceof Customization) {
                useCustomization(edge);
            } else if (edge.getCurrent() instanceof CustomizerSource) {
                useCustomizerSource(element, edge);
            }
        }
    }

    private void useCustomization(Edge<Annotation> edge) {
        Customization customization = (Customization) edge.getCurrent();
        for (Class<? extends Customizer> type : customization.value()) {
            applyCustomizer(instantiate(type));
        }
    }

    private void useCustomizerSource(
        AnnotatedElement element,
        Edge<Annotation> edge
    ) {
        CustomizerSource source = (CustomizerSource) edge.getCurrent();
        CustomizerFactory factory = instantiate(source.value());
        edge.useParent(parent -> consumeAnnotationIfMatch(factory, parent));
        consumeAnnotatedElementIfMatch(factory, element);
        applyCustomizer(factory.createCustomizer());
    }

    private static void consumeAnnotatedElementIfMatch(
        CustomizerFactory factory,
        AnnotatedElement element
    ) {
        if (factory instanceof AnnotatedElementConsumer) {
            ((AnnotatedElementConsumer) factory).accept(element);
        }
    }

    public Arguments getTestCase(Arguments asset) {
        return getTestCase(asset.get());
    }

    public Arguments getTestCase(Object[] asset) {
        Brake brake = getBrake();
        Parameter[] parameters = getTestParameters();
        List<Object> arguments = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (brake.shouldBrakeBefore(parameter)) {
                break;
            }

            arguments.add(resolveArgument(getQuery(parameter, i), asset));
        }

        return Arguments.of(arguments.toArray());
    }

    private ParameterQuery getQuery(Parameter parameter, int index) {
        Type type = parameter.getParameterizedType();

        if (type instanceof TypeVariable) {
            type = resolveTypeVariable((TypeVariable<?>) type);
        }

        return new ParameterQuery(parameter, index, type);
    }

    private Type resolveTypeVariable(TypeVariable<?> typeVariable) {
        ExtensionContext extensionContext = resolve(ExtensionContext.class);
        Class<?> testClass = extensionContext.getRequiredTestClass();

        Type resolvedType = resolveTypeVariableFromClass(testClass, typeVariable);
        if (resolvedType != null) {
            return resolvedType;
        }

        Type[] bounds = typeVariable.getBounds();
        Type fallbackType = bounds.length > 0 ? bounds[0] : Object.class;

        String message = String.format(
            "Failed to resolve type variable '%s' from test class "
            + "hierarchy '%s'. Falling back to %s. "
            + "This may indicate missing type parameter mapping in your "
            + "test class hierarchy. For example, if you have "
            + "'class MyTest extends BaseTest<T>', make sure to specify "
            + "the type parameter like 'class MyTest extends BaseTest<MyType>'.",
            typeVariable.getName(),
            testClass.getName(),
            TypeFormatter.format(fallbackType, false)
        );

        System.err.println("WARNING: " + message);

        return fallbackType;
    }

    private Type resolveTypeVariableFromClass(
        Class<?> clazz,
        TypeVariable<?> typeVariable
    ) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }

        Type genericSuperclass = clazz.getGenericSuperclass();
        Type resolvedType = resolveTypeVariableFromType(
            genericSuperclass,
            typeVariable
        );
        if (resolvedType != null) {
            return resolvedType;
        }

        for (Type genericInterface : clazz.getGenericInterfaces()) {
            resolvedType = resolveTypeVariableFromType(
                genericInterface,
                typeVariable
            );
            if (resolvedType != null) {
                return resolvedType;
            }
        }

        if (genericSuperclass instanceof Class<?>) {
            return resolveTypeVariableFromClass(
                (Class<?>) genericSuperclass,
                typeVariable
            );
        }

        return null;
    }

    private Type resolveTypeVariableFromType(
        Type type,
        TypeVariable<?> typeVariable
    ) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();

        if (!(rawType instanceof Class<?>)) {
            return null;
        }

        Class<?> rawClass = (Class<?>) rawType;
        TypeVariable<?>[] typeParameters = rawClass.getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < typeParameters.length; i++) {
            if (typeParameters[i].equals(typeVariable)) {
                Type resolvedType = actualTypeArguments[i];
                if (resolvedType instanceof TypeVariable<?>) {
                    return resolveTypeVariable((TypeVariable<?>) resolvedType);
                }
                return resolvedType;
            }
        }

        return resolveTypeVariableFromClass(rawClass, typeVariable);
    }

    private Brake getBrake() {
        List<Brake> brakes = new ArrayList<>();
        ExtensionContext extensionContext = resolve(ExtensionContext.class);
        collectBrakes(extensionContext.getRequiredTestMethod(), brakes::add);
        brakes.add(TestGearBrake.INSTANCE);
        return Brake.compose(brakes.toArray(new Brake[0]));
    }

    private Parameter[] getTestParameters() {
        ExtensionContext extensionContext = resolve(ExtensionContext.class);
        return extensionContext.getRequiredTestMethod().getParameters();
    }

    public Object resolveArgument(Parameter parameter, int index) {
        return resolveArgument(getQuery(parameter, index), EMPTY_ASSET);
    }

    private Object resolveArgument(ParameterQuery query, Object[] asset) {
        applyAnnotatedCustomizers(query.getParameter());
        Object argument = query.getIndex() < asset.length
            ? convertAsset(query, asset[query.getIndex()])
            : resolve(query);
        recycleArgument(argument, query);
        return argument;
    }

    private void recycleArgument(Object argument, ParameterQuery query) {
        if (argument instanceof Named<?>) {
            recycleArgument((Named<?>) argument, query);
            return;
        }

        for (Edge<RecycleArgument> edge :
            scanAnnotations(query.getParameter(), RecycleArgument.class)
        ) {
            recycleArgument(argument, edge, query);
        }
    }

    private void recycleArgument(Named<?> argument, ParameterQuery query) {
        recycleArgument(argument.getPayload(), query);
    }

    private void recycleArgument(
        Object argument,
        Edge<RecycleArgument> edge,
        ParameterQuery query
    ) {
        ArgumentRecycler recycler = instantiate(edge.getCurrent().value());
        edge.useParent(parent -> consumeAnnotationIfMatch(recycler, parent));
        applyCustomizer(recycler.recycle(argument, query.getParameter()));
    }

    private Object convertAsset(ParameterQuery query, Object asset) {
        return asset instanceof Named<?>
            ? convertAsset(query, (Named<?>) asset)
            : resolve(AssetConverter.class).convert(query, asset);
    }

    private Object convertAsset(ParameterQuery query, Named<?> namedAsset) {
        Object asset = convertAsset(query, namedAsset.getPayload());
        return Named.of(namedAsset.getName(), asset);
    }
}
