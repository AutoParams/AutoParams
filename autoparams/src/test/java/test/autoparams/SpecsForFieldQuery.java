package test.autoparams;

import java.lang.reflect.Field;

import autoparams.FieldQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForFieldQuery {

    @AllArgsConstructor
    @Getter
    static class Container {

        private final String field;
    }

    @Test
    void sut_returns_field() {
        Field field = getField();
        FieldQuery sut = new FieldQuery(field, field.getType());
        assertThat(sut.getField()).isSameAs(field);
    }

    @Test
    void sut_returns_type() {
        Field field = getField();
        FieldQuery sut = new FieldQuery(field, field.getType());
        assertThat(sut.getType()).isSameAs(field.getType());
    }

    private Field getField() {
        try {
            return Container.class.getDeclaredField("field");
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }
}
