package test.autoparams.customization;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import autoparams.AutoSource;
import autoparams.customization.ArgumentRecycler;
import autoparams.customization.Customizer;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentRecycler {

    public static class Record {

        public final Object argument;
        public final ParameterContext context;

        public Record(Object argument, ParameterContext context) {
            this.argument = argument;
            this.context = context;
        }
    }

    @ParameterizedTest
    @AutoSource
    void apply_correctly_passes_arguments_to_recycle(Object argument) {
        List<Record> records = new ArrayList<>();
        ParameterContext context = createParameterContext();
        ArgumentRecycler sut = (a, c) -> {
            records.add(new Record(a, c));
            return createCustomizer();
        };

        sut.apply(argument, context);

        assertThat(records).hasSize(1);
        assertThat(records.get(0).argument).isSameAs(argument);
        assertThat(records.get(0).context).isSameAs(context);
    }

    @ParameterizedTest
    @AutoSource
    void apply_returns_customizer_returned_by_recycle(Object argument) {
        Customizer customizer = createCustomizer();
        ArgumentRecycler sut = (a, c) -> customizer;

        Customizer actual = sut.apply(argument, createParameterContext());

        assertThat(actual).isSameAs(customizer);
    }

    private ParameterContext createParameterContext() {
        return (ParameterContext) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class<?>[] { ParameterContext.class },
            (proxy, method, args) -> method.getDefaultValue()
        );
    }

    private Customizer createCustomizer() {
        return (ObjectProcessor) (q, v, c) -> { };
    }
}
