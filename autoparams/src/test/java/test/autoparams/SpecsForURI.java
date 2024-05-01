package test.autoparams;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import autoparams.AutoSource;
import autoparams.MethodAutoSource;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.Factory;
import autoparams.generator.ObjectGeneratorBase;
import autoparams.generator.URIGenerationOptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SpecsForURI {

    public static final class OptionsProvider
        extends ObjectGeneratorBase<URIGenerationOptions> {

        private final URIGenerationOptions options;

        public OptionsProvider(
            String[] schemes,
            String[] hosts,
            int[] ports
        ) {
            options = new URIGenerationOptions(schemes, hosts, ports);
        }

        @Override
        protected URIGenerationOptions generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return options;
        }
    }

    @ParameterizedTest
    @MethodAutoSource("getSchemes")
    void sut_consumes_protocols_option(
        String[] schemes,
        Factory<URI> factory
    ) {
        String[] hosts = { "test.com" };
        int[] ports = { };
        OptionsProvider optionsProvider = new OptionsProvider(
            schemes,
            hosts,
            ports
        );
        factory.applyCustomizer(optionsProvider);

        List<String> actual = factory
            .stream()
            .map(URI::getScheme)
            .limit(100)
            .distinct()
            .collect(toList());

        assertThat(actual).hasSameSizeAs(schemes);
        assertThat(actual).allMatch(x -> asList(schemes).contains(x));
    }

    static Stream<Arguments> getSchemes() {
        return Stream.of(
            arguments((Object) new String[] { "https", "http", "ftp" }),
            arguments((Object) new String[] { "https", "http" }),
            arguments((Object) new String[] { "ftp" })
        );
    }

    @ParameterizedTest
    @MethodAutoSource("getHosts")
    void sut_consumes_hosts_option(String[] hosts, Factory<URI> factory) {
        String[] schemes = { "https" };
        int[] ports = { };
        OptionsProvider optionsProvider = new OptionsProvider(
            schemes,
            hosts,
            ports
        );
        factory.applyCustomizer(optionsProvider);

        List<String> actual = factory
            .stream()
            .map(URI::getHost)
            .limit(100)
            .distinct()
            .collect(toList());

        assertThat(actual).hasSameSizeAs(hosts);
        assertThat(actual).allMatch(x -> asList(hosts).contains(x));
    }

    static Stream<Arguments> getHosts() {
        return Stream.of(
            arguments((Object) new String[] { "test.com" }),
            arguments((Object) new String[] { "test.com", "example.com" }),
            arguments((Object) new String[] { "example.com", "test.com" })
        );
    }

    @ParameterizedTest
    @AutoSource
    void sut_omits_port_if_port_is_not_specified(URI uri) {
        assertThat(uri.getPort()).isEqualTo(-1);
    }

    @ParameterizedTest
    @MethodAutoSource("getPorts")
    void sut_consumes_ports_option(int[] ports, Factory<URI> factory) {
        String[] schemes = { "https" };
        String[] hosts = { "test.com" };
        OptionsProvider optionsProvider = new OptionsProvider(
            schemes,
            hosts,
            ports
        );
        factory.applyCustomizer(optionsProvider);

        List<Integer> actual = factory
            .stream()
            .map(URI::getPort)
            .limit(100)
            .distinct()
            .collect(toList());

        assertThat(actual).hasSameSizeAs(ports);
        Integer[] boxedPorts = stream(ports).boxed().toArray(Integer[]::new);
        assertThat(actual).allMatch(x -> asList(boxedPorts).contains(x));
    }

    @SuppressWarnings("RedundantCast")
    static Stream<Arguments> getPorts() {
        return Stream.of(
            arguments((Object) new int[] { 80 }),
            arguments((Object) new int[] { 80, 443 }),
            arguments((Object) new int[] { 443, 80, 8080 })
        );
    }
}
