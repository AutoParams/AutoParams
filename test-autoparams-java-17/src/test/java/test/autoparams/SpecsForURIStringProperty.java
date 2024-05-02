package test.autoparams;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForURIStringProperty {

    public record Properties(String imageUri, String videoUrl) { }

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

    public record ImageUris(String thumbnail, String fullSize) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_uri_string_for_string_properties_of_type_with_uris_suffix(
        ImageUris uris
    ) {
        String regex = "^(http|https|ftp)://[^\\s/$.?#].\\S*$";
        assertThat(uris.thumbnail().matches(regex)).isTrue();
        assertThat(uris.fullSize().matches(regex)).isTrue();
    }

    public record ImageUrls(String thumbnail, String fullSize) { }

    @ParameterizedTest
    @AutoSource
    void sut_generates_uri_string_for_string_properties_of_type_with_urls_suffix(
        ImageUrls urls
    ) {
        String regex = "^(http|https|ftp)://[^\\s/$.?#].\\S*$";
        assertThat(urls.thumbnail().matches(regex)).isTrue();
        assertThat(urls.fullSize().matches(regex)).isTrue();
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class PictureUris {

        public final String thumbnail;
        public final String fullSize;

        public PictureUris(String thumbnail, String fullSize) {
            this.thumbnail = thumbnail;
            this.fullSize = fullSize;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_support_properties_of_non_record_type(PictureUris uris) {
        String regex = "^(http|https|ftp)://[^\\s/$.?#].\\S*$";
        assertThat(uris.thumbnail.matches(regex)).isFalse();
        assertThat(uris.fullSize.matches(regex)).isFalse();
    }
}
