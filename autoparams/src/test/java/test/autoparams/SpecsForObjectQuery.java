package test.autoparams;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectQuery {

    @Test
    void toLog_returns_class_name_with_package_when_verbose_is_true() {
        ObjectQuery sut = new DefaultObjectQuery(String.class);
        String actual = sut.toLog(true);
        assertThat(actual).isEqualTo("java.lang.String");
    }

    @Test
    void toLog_returns_simple_class_name_when_verbose_is_false() {
        ObjectQuery sut = new DefaultObjectQuery(String.class);
        String actual = sut.toLog(false);
        assertThat(actual).isEqualTo("String");
    }
}
