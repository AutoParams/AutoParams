package test.autoparams;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForURIStringProperty {

    public record Properties(String imageUri, String videoUrl) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_generates_uri_string_for_uri_suffix(Properties properties) {
        String regex = "^(http|https|ftp)://[^\\s/$.?#].\\S*$";
        assertThat(properties.imageUri().matches(regex)).isTrue();
    }

    @ParameterizedTest
    @AutoSource
    void sut_generates_uri_string_for_url_suffix(Properties properties) {
        String regex = "^(http|https|ftp)://[^\\s/$.?#].\\S*$";
        assertThat(properties.videoUrl().matches(regex)).isTrue();
    }
}
