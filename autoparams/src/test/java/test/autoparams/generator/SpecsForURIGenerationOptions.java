package test.autoparams.generator;

import java.util.stream.Stream;

import autoparams.AutoSource;
import autoparams.generator.URIGenerationOptions;
import jakarta.validation.constraints.Max;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SpecsForURIGenerationOptions {

    @SuppressWarnings("DataFlowIssue")
    @Test
    void constructor_has_guard_against_null_schemes() {
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(null, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("schemes");
    }

    @Test
    void constructor_has_guard_against_empty_schemes() {
        String[] schemes = { };
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("schemes");
    }

    @Test
    void constructor_has_guard_against_null_element_in_protocol() {
        String[] schemes = { null };
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("schemes");
    }

    @Test
    void constructor_has_guard_against_null_hosts() {
        String[] schemes = { "https" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, null, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_empty_hosts() {
        String[] schemes = { "https" };
        String[] hosts = { };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_null_element_in_host() {
        String[] schemes = { "https" };
        String[] hosts = { null };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_null_ports() {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, null);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ports");
    }

    @ParameterizedTest
    @AutoSource
    void constructor_has_guard_against_negative_element_in_ports(
        @Max(-1) int port
    ) {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { port };

        ThrowingCallable callable = () ->
            new URIGenerationOptions(schemes, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ports");
    }

    @ParameterizedTest
    @MethodSource("getSchemes")
    void constructor_correctly_sets_schemes(String[] schemes) {
        String[] hosts = { "test.com" };
        int[] ports = { };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        assertThat(sut.schemes()).containsExactlyInAnyOrder(schemes);
    }

    static Stream<Arguments> getSchemes() {
        return Stream.of(
            arguments((Object) new String[] { "https", "http", "ftp" }),
            arguments((Object) new String[] { "https", "http" }),
            arguments((Object) new String[] { "ftp" })
        );
    }

    @ParameterizedTest
    @MethodSource("getHosts")
    void constructor_correctly_sets_hosts(String[] hosts) {
        String[] schemes = { "https" };
        int[] ports = { };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        assertThat(sut.hosts()).containsExactlyInAnyOrder(hosts);
    }

    static Stream<Arguments> getHosts() {
        return Stream.of(
            arguments((Object) new String[] { "test.com" }),
            arguments((Object) new String[] { "test.com", "example.com" }),
            arguments((Object) new String[] { "example.com", "test.com" })
        );
    }

    @ParameterizedTest
    @MethodSource("getPorts")
    void constructor_correctly_sets_ports(int[] ports) {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        Integer[] boxedPorts = stream(ports).boxed().toArray(Integer[]::new);
        assertThat(sut.ports()).containsExactlyInAnyOrder(boxedPorts);
    }

    @SuppressWarnings("RedundantCast")
    static Stream<Arguments> getPorts() {
        return Stream.of(
            arguments((Object) new int[] { 80 }),
            arguments((Object) new int[] { 80, 443 }),
            arguments((Object) new int[] { 443, 80 })
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void schemes_returns_immutable_list() {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.schemes().set(0, "ftp"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void hosts_returns_immutable_list() {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.hosts().set(0, "new.host"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void ports_returns_immutable_list() {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { 443 };

        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.ports().set(0, 80))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void default_instance_has_single_scheme() {
        URIGenerationOptions sut = URIGenerationOptions.DEFAULT;
        assertThat(sut.schemes()).containsExactlyInAnyOrder("https");
    }

    @Test
    void default_instance_has_single_host() {
        URIGenerationOptions sut = URIGenerationOptions.DEFAULT;
        assertThat(sut.hosts()).containsExactlyInAnyOrder("test.com");
    }

    @Test
    void default_instance_has_no_ports() {
        URIGenerationOptions sut = URIGenerationOptions.DEFAULT;
        assertThat(sut.ports()).isEmpty();
    }

    @Test
    void toString_displays_all_information_for_default_instance() {
        // Arrange
        URIGenerationOptions sut = URIGenerationOptions.DEFAULT;

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual)
            .startsWith("URIGenerationOptions")
            .contains("schemes=[\"https\"]")
            .contains("hosts=[\"test.com\"]")
            .contains("ports=[]");
    }

    @Test
    void toString_properly_formats_the_schemes_list_with_square_brackets() {
        // Arrange
        String[] schemes = { "http", "https", "ftp" };
        URIGenerationOptions sut = new URIGenerationOptions(
            schemes,
            new String[] { "example.com" },
            new int[] { 80 }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).contains("schemes=[\"http\", \"https\", \"ftp\"]");
    }

    @Test
    void toString_properly_formats_the_hosts_list_with_square_brackets() {
        // Arrange
        String[] hosts = { "example.com", "test.org", "localhost" };
        URIGenerationOptions sut = new URIGenerationOptions(
            new String[] { "https" },
            hosts,
            new int[] { 443 }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual)
            .contains("hosts=[\"example.com\", \"test.org\", \"localhost\"]");
    }

    @Test
    void toString_properly_formats_the_ports_list_with_square_brackets() {
        // Arrange
        int[] ports = { 80, 443, 8080 };
        URIGenerationOptions sut = new URIGenerationOptions(
            new String[] { "https" },
            new String[] { "example.com" },
            ports
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).contains("ports=[80, 443, 8080]");
    }

    @Test
    void toString_separates_fields_with_commas() {
        // Arrange
        URIGenerationOptions sut = new URIGenerationOptions(
            new String[] { "http", "https" },
            new String[] { "example.com" },
            new int[] { 80, 443 }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).contains("schemes=");
        assertThat(actual).contains(", hosts=");
        assertThat(actual).contains(", ports=");
    }

    @Test
    void toString_surrounds_the_entire_output_with_brackets_after_the_class_name() {
        // Arrange
        URIGenerationOptions sut = new URIGenerationOptions(
            new String[] { "http" },
            new String[] { "example.com" },
            new int[] { 80 }
        );

        // Act
        String actual = sut.toString();

        // Assert
        assertThat(actual).matches("URIGenerationOptions\\[.*]");
    }
}
