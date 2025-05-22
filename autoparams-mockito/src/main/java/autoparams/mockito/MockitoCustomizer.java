package autoparams.mockito;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

/**
 * A customizer that creates test double objects using
 * <a href="https://site.mockito.org/">Mockito</a>.
 * <p>
 * This customizer integrates the Mockito with AutoParams to automatically
 * generate test doubles during test parameter generation. The created test
 * doubles are configured to call real methods by default.
 * </p>
 *
 * <p>
 * <b>Example: Generate test doubles with Mockito in tests automatically</b>
 * </p>
 * <p>
 * This example shows how to use the MockitoCustomizer to inject the test double
 * created by Mockito into the system under test:
 * </p>
 * <pre>
 * public interface Dependency {
 *
 *     String getName();
 * }
 *
 * public class SystemUnderTest {
 *
 *     private final Dependency dependency;
 *
 *     public SystemUnderTest(Dependency dependency) {
 *         this.dependency = dependency;
 *     }
 *
 *     public String getMessage() {
 *         return "Hello " + dependency.getName();
 *     }
 * }
 *
 * &#64;Test
 * &#64;AutoParams
 * &#64;Customization(MockitoCustomizer.class)
 * void testMethod(&#64;Freeze Dependency stub, SystemUnderTest sut) {
 *     when(stub.getName()).thenReturn("World");
 *     assertEquals("Hello World", sut.getMessage());
 * }
 * </pre>
 *
 * @see org.mockito.Mockito
 * @see Customizer
 * @see autoparams.customization.Freeze
 */
public final class MockitoCustomizer implements Customizer {

    /**
     * Creates an instance of {@link MockitoCustomizer}.
     */
    public MockitoCustomizer() {
    }

    /**
     * Customizes the provided {@link ObjectGenerator} to enable the creation of
     * test double objects using Mockito.
     * <p>
     * This method wraps the original generator. If the original generator fails
     * to produce a value for a requested type, this customized generator
     * attempts to create a test double object for that type. This is
     * particularly useful for interfaces or abstract classes that cannot be
     * instantiated directly.
     * </p>
     * <p>
     * The test double objects are created with the
     * {@link org.mockito.Mockito#CALLS_REAL_METHODS} setting, meaning that real methods
     * will be invoked unless explicitly stubbed.
     * </p>
     *
     * @param generator The original {@link ObjectGenerator} to be customized.
     * @return A new {@link ObjectGenerator} that incorporates Mockito-based
     *         test double object generation.
     */
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> generator
            .generate(query, context)
            .yieldIfEmpty(() -> generate(query.getType()));
    }

    private ObjectContainer generate(Type type) {
        return type instanceof Class<?>
            ? generate((Class<?>) type)
            : type instanceof ParameterizedType
            ? generate((ParameterizedType) type)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(Class<?> type) {
        return new ObjectContainer(mock(type, CALLS_REAL_METHODS));
    }

    private ObjectContainer generate(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getRawType();
        return type instanceof Class<?>
            ? generate((Class<?>) type)
            : ObjectContainer.EMPTY;
    }
}
