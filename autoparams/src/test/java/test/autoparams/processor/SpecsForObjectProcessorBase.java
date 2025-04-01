package test.autoparams.processor;

import java.util.concurrent.atomic.AtomicInteger;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.ObjectProcessor;
import autoparams.processor.ObjectProcessorBase;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectProcessorBase {

    @Getter
    @Setter
    public static class User {

        private String name;
        private String email;
        private String encodedPassword;
    }

    @Getter
    @Setter
    public static class Employee extends User {

        private int salary;
    }

    @FunctionalInterface
    public interface Processor<T> {

        void process(ObjectQuery query, T value, ResolutionContext context);
    }

    public static class DelegatingUserProcessor
        extends ObjectProcessorBase<User> {

        private final Processor<User> func;

        public DelegatingUserProcessor(Processor<User> func) {
            this.func = func;
        }

        @Override
        protected void processObject(
            ObjectQuery query,
            User value,
            ResolutionContext context
        ) {
            func.process(query, value, context);
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_calls_implementation_method_if_types_match(
        AtomicInteger counter,
        ResolutionContext context,
        User value
    ) {
        ObjectQuery query = new DefaultObjectQuery(User.class);
        ObjectProcessor sut = new DelegatingUserProcessor(
            (q, v, c) -> counter.incrementAndGet()
        );

        sut.process(query, value, context);

        assertThat(counter.get()).isEqualTo(1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_call_implementation_method_if_types_do_not_match(
        AtomicInteger counter,
        ResolutionContext context,
        Integer value
    ) {
        ObjectQuery query = new DefaultObjectQuery(Integer.class);
        ObjectProcessor sut = new DelegatingUserProcessor(
            (q, v, c) -> counter.incrementAndGet()
        );

        sut.process(query, value, context);

        assertThat(counter.get()).isEqualTo(0);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_passes_arguments(
        ResolutionContext context,
        User value
    ) {
        ObjectQuery query = new DefaultObjectQuery(User.class);
        new DelegatingUserProcessor(
            (q, v, c) -> {
                assertThat(q).isSameAs(query);
                assertThat(v).isSameAs(value);
                assertThat(c).isSameAs(context);
            }
        ).process(query, value, context);
    }

    @ParameterizedTest
    @AutoSource
    void sut_supports_sub_types(
        AtomicInteger counter,
        ResolutionContext context,
        Employee value
    ) {
        ObjectQuery query = new DefaultObjectQuery(Employee.class);
        ObjectProcessor sut = new DelegatingUserProcessor(
            (q, v, c) -> counter.incrementAndGet()
        );

        sut.process(query, value, context);

        assertThat(counter.get()).isEqualTo(1);
    }
}
