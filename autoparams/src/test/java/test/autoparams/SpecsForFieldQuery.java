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

    @Test
    void toString_returns_value_that_starts_with_correct_prefix() {
        Field field = getField();
        FieldQuery sut = new FieldQuery(field, field.getType());
        String actual = sut.toString();
        assertThat(actual).startsWith("Field ");
    }

    @Test
    void toString_returns_value_that_contains_type_of_field() {
        Field field = getField();
        FieldQuery sut = new FieldQuery(field, field.getType());
        String actual = sut.toString();
        assertThat(actual).contains(field.getType().getTypeName());
    }

    @Test
    void toString_returns_value_that_contains_name_of_field() {
        Field field = getField();
        FieldQuery sut = new FieldQuery(field, field.getType());
        String actual = sut.toString();
        assertThat(actual).contains(field.getName());
    }

    private Field getField() {
        try {
            return Container.class.getDeclaredField("field");
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }
}
