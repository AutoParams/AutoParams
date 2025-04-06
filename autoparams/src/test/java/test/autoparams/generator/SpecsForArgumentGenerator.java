package test.autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import autoparams.AutoParams;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ArgumentGenerator;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentGenerator {

    @Test
    void sut_extends_ObjectGenerator() {
        Class<?> sut = ArgumentGenerator.class;
        assertThat(ObjectGenerator.class).isAssignableFrom(sut);
    }

    @Test
    void sut_is_functional_interface() {
        Class<?> sut = ArgumentGenerator.class;
        assertThat(sut.isAnnotationPresent(FunctionalInterface.class)).isTrue();
    }

    public static class ArgumentGeneratorSpy implements ArgumentGenerator {

        @AllArgsConstructor
        @Getter
        public static class Record {

            private final ParameterQuery query;
            private final ResolutionContext context;
        }

        private final List<Record> recordList = new ArrayList<>();

        @Getter
        private final List<Record> records = unmodifiableList(recordList);

        @Override
        public ObjectContainer generate(
            ParameterQuery query,
            ResolutionContext context
        ) {
            recordList.add(new Record(query, context));
            return ObjectContainer.EMPTY;
        }
    }

    @Test
    void default_generate_invokes_abstract_generate_with_ParameterQuery() {
        ResolutionContext context = new ResolutionContext();
        ArgumentGeneratorSpy spy = new ArgumentGeneratorSpy();
        ArgumentGenerator sut = spy::generate;
        ObjectQuery query = getParameterQuery();

        sut.generate(query, context);

        assertThat(spy.getRecords()).hasSize(1);
        assertThat(spy.getRecords().get(0).getQuery()).isSameAs(query);
        assertThat(spy.getRecords().get(0).getContext()).isSameAs(context);
    }

    @Test
    @AutoParams
    void default_generate_returns_value_from_abstract_generate(Object value) {
        ResolutionContext context = new ResolutionContext();
        ArgumentGenerator sut = (q, c) -> new ObjectContainer(value);
        ObjectQuery query = getParameterQuery();

        Object actual = sut.generate(query, context).unwrapOrElseThrow();

        assertThat(actual).isSameAs(value);
    }

    @Test
    void default_generate_does_not_invokes_abstract_generate_with_non_ParameterQuery() {
        ResolutionContext context = new ResolutionContext();
        ArgumentGeneratorSpy spy = new ArgumentGeneratorSpy();
        ArgumentGenerator sut = spy::generate;
        ObjectQuery query = () -> String.class;

        sut.generate(query, context);

        assertThat(spy.getRecords()).isEmpty();
    }

    @Test
    void default_generate_returns_empty_with_non_ParameterQuery() {
        ResolutionContext context = new ResolutionContext();
        ArgumentGenerator sut = (q, c) -> ObjectContainer.EMPTY;
        ObjectQuery query = () -> String.class;

        ObjectContainer actual = sut.generate(query, context);

        assertThat(actual).isSameAs(ObjectContainer.EMPTY);
    }

    private ParameterQuery getParameterQuery() {
        try {
            Method method = ArgumentGenerator.class.getDeclaredMethod(
                "generate",
                ParameterQuery.class,
                ResolutionContext.class
            );
            int index = 0;
            Parameter parameter = method.getParameters()[index];
            return new ParameterQuery(
                parameter,
                index,
                parameter.getParameterizedType()
            );
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
