package test.autoparams.processor;

import java.util.ArrayList;
import java.util.List;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.CompositeObjectProcessor;
import autoparams.processor.ObjectProcessor;
import lombok.Getter;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.HasSetter;

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SpecsForCompositeObjectProcessor {

    @Getter
    public static class Record {

        private final ObjectQuery query;
        private final Object value;
        private final ResolutionContext context;

        public Record(
            ObjectQuery query,
            Object value,
            ResolutionContext context
        ) {
            this.query = query;
            this.value = value;
            this.context = context;
        }
    }

    public static class Spy implements ObjectProcessor {

        private final List<Record> records = new ArrayList<>();
        private final List<Record> recordsView = unmodifiableList(records);

        @Override
        public void process(
            ObjectQuery query,
            Object value,
            ResolutionContext context
        ) {
            records.add(new Record(query, value, context));
        }

        public List<Record> getRecords() {
            return recordsView;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_invokes_all_processors(
        Spy processor1,
        Spy processor2,
        Spy processor3,
        Object value,
        ResolutionContext context
    ) {
        // Arrange
        ObjectProcessor sut = new CompositeObjectProcessor(
            processor1,
            processor2,
            processor3
        );

        ObjectQuery query = new DefaultObjectQuery(String.class);

        // Act
        sut.process(query, value, context);

        // Assert
        assertThat(processor1.getRecords()).hasSize(1);
        assertThat(processor1.getRecords().get(0).getQuery()).isEqualTo(query);
        assertThat(processor1.getRecords().get(0).getValue()).isEqualTo(value);
        assertThat(processor1.getRecords().get(0).getContext()).isEqualTo(context);

        assertThat(processor2.getRecords()).hasSize(1);
        assertThat(processor2.getRecords().get(0).getQuery()).isEqualTo(query);
        assertThat(processor2.getRecords().get(0).getValue()).isEqualTo(value);
        assertThat(processor2.getRecords().get(0).getContext()).isEqualTo(context);

        assertThat(processor3.getRecords()).hasSize(1);
        assertThat(processor3.getRecords().get(0).getQuery()).isEqualTo(query);
        assertThat(processor3.getRecords().get(0).getValue()).isEqualTo(value);
        assertThat(processor3.getRecords().get(0).getContext()).isEqualTo(context);
    }

    @ParameterizedTest
    @AutoSource
    void sut_invokes_all_processors_in_order(
        HasSetter value,
        ResolutionContext context
    ) {
        // Arrange
        ObjectProcessor sut = new CompositeObjectProcessor(
            (q, v, c) -> ((HasSetter) v).setValue("1"),
            (q, v, c) -> ((HasSetter) v).setValue("2")
        );

        // Act
        sut.process(new DefaultObjectQuery(HasSetter.class), value, context);

        // Assert
        assertThat(value.getValue()).isEqualTo("2");
    }
}
