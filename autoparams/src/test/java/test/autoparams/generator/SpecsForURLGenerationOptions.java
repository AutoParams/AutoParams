package test.autoparams.generator;

import java.util.stream.Stream;
import javax.validation.constraints.Max;

import autoparams.AutoSource;
import autoparams.generator.URLGenerationOptions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SpecsForURLGenerationOptions {

    @SuppressWarnings("DataFlowIssue")
    @Test
    void constructor_has_guard_against_null_protocols() {
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(null, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("protocols");
    }

    @Test
    void constructor_has_guard_against_empty_protocols() {
        String[] protocols = { };
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("protocols");
    }

    @Test
    void constructor_has_guard_against_null_element_in_protocol() {
        String[] protocols = { null };
        String[] hosts = { "test.com" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("protocols");
    }

    @Test
    void constructor_has_guard_against_null_hosts() {
        String[] protocols = { "https" };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, null, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_empty_hosts() {
        String[] protocols = { "https" };
        String[] hosts = { };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_null_element_in_host() {
        String[] protocols = { "https" };
        String[] hosts = { null };
        int[] ports = { };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hosts");
    }

    @Test
    void constructor_has_guard_against_null_ports() {
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, null);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ports");
    }

    @ParameterizedTest
    @AutoSource
    void constructor_has_guard_against_negative_element_in_ports(
        @Max(-1) int port
    ) {
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { port };

        ThrowingCallable callable = () ->
            new URLGenerationOptions(protocols, hosts, ports);

        assertThatThrownBy(callable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ports");
    }

    @ParameterizedTest
    @MethodSource("getProtocols")
    void constructor_correctly_sets_protocols(String[] protocols) {
        String[] hosts = { "test.com" };
        int[] ports = { };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
            hosts,
            ports
        );

        assertThat(sut.protocols()).containsExactlyInAnyOrder(protocols);
    }

    static Stream<Arguments> getProtocols() {
        return Stream.of(
            arguments((Object) new String[] { "https", "http", "ftp" }),
            arguments((Object) new String[] { "https", "http" }),
            arguments((Object) new String[] { "ftp" })
        );
    }

    @ParameterizedTest
    @MethodSource("getHosts")
    void constructor_correctly_sets_hosts(String[] hosts) {
        String[] protocols = { "https" };
        int[] ports = { };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
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
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
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

    @Test
    void protocols_returns_immutable_list() {
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.protocols().set(0, "ftp"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void hosts_returns_immutable_list() {
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.hosts().set(0, "new.host"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void ports_returns_immutable_list() {
        String[] protocols = { "https" };
        String[] hosts = { "test.com" };
        int[] ports = { 443 };

        URLGenerationOptions sut = new URLGenerationOptions(
            protocols,
            hosts,
            ports
        );

        assertThatThrownBy(() -> sut.ports().set(0, 80))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void default_instance_has_single_protocol() {
        URLGenerationOptions sut = URLGenerationOptions.DEFAULT;
        assertThat(sut.protocols()).containsExactlyInAnyOrder("https");
    }

    @Test
    void default_instance_has_single_host() {
        URLGenerationOptions sut = URLGenerationOptions.DEFAULT;
        assertThat(sut.hosts()).containsExactlyInAnyOrder("test.com");
    }

    @Test
    void default_instance_has_no_ports() {
        URLGenerationOptions sut = URLGenerationOptions.DEFAULT;
        assertThat(sut.ports()).isEmpty();
    }
}
