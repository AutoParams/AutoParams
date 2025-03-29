package test.autoparams.customization;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import autoparams.AutoSource;
import autoparams.customization.ArgumentRecycler;
import autoparams.customization.Customizer;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentRecycler {

    public static class Record {

        public final Object argument;
        public final Parameter parameter;

        public Record(Object argument, Parameter parameter) {
            this.argument = argument;
            this.parameter = parameter;
        }
    }

    @ParameterizedTest
    @AutoSource
    void apply_correctly_passes_arguments_to_recycle(Object argument) {
        List<Record> records = new ArrayList<>();
        Parameter parameter = getParameter();
        ArgumentRecycler sut = (a, p) -> {
            records.add(new Record(a, p));
            return createCustomizer();
        };

        sut.apply(argument, parameter);

        assertThat(records).hasSize(1);
        assertThat(records.get(0).argument).isSameAs(argument);
        assertThat(records.get(0).parameter).isSameAs(parameter);
    }

    @ParameterizedTest
    @AutoSource
    void apply_returns_customizer_returned_by_recycle(Object argument) {
        Customizer customizer = createCustomizer();
        ArgumentRecycler sut = (a, p) -> customizer;

        Customizer actual = sut.apply(argument, getParameter());

        assertThat(actual).isSameAs(customizer);
    }

    private static Parameter getParameter() {
        return Record.class.getConstructors()[0].getParameters()[0];
    }

    private Customizer createCustomizer() {
        return (ObjectProcessor) (q, v, c) -> { };
    }
}
