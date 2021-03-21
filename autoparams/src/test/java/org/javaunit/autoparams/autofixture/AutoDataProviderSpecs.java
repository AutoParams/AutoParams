package org.javaunit.autoparams.autofixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.javaunit.autoparams.autofixture.AutoDAtaProviderSpecsUtil.getAutoParamFirstArgument;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutoDataProviderSpecs {

    @Test
    void sut_for_boolean() {
        long trueCount = IntStream.of(100).boxed()
            .map(x -> getAutoParamFirstArgument("test_boolean"))
            .filter(x -> (boolean) x)
            .count();
        boolean result = trueCount > 0;
        assertThat(result).isTrue();
    }

    void test_boolean(boolean a) {}

    @Test
    void sut_for_vo() {
        TestVo vo = (TestVo)getAutoParamFirstArgument("test_object");
        assertThat(vo.a).isNotNull();
        assertThat(vo.b).isNotNull();
    }

    void test_object(TestVo a) {}

    class TestVo {
        public TestVo() {}
        public TestVo(int a) {}
        public TestVo(int a, String b) {}

        Boolean a = null;
        Integer b = null;
    }

}

class AutoDAtaProviderSpecsUtil {

    // below methods is utility for test
    protected static Object getAutoParamFirstArgument(String methodName) {
        AutoDataProvider provider = new AutoDataProvider();
        return provider.provideArguments(getExtensionContext(methodName))
            .map(x -> x.get()[0])
            .findFirst()
            .orElse(null);
    }

    private static ExtensionContext getExtensionContext(String methodName) {
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getTestMethod()).thenReturn(Optional.of(getMethod(methodName)));
        return context;
    }

    private static Method getMethod(String methodName) {
        Method[] methods = AutoDataProviderSpecs.class.getDeclaredMethods();
        return Stream.of(methods).filter(x -> x.getName().equals(methodName)).findFirst().orElse(null);
    }
}
