package autoparams.generator;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class URIGenerationOptions {

    public static final URIGenerationOptions DEFAULT =
        new URIGenerationOptions(
            new String[] { "https" },
            new String[] { "test.com" },
            new int[] { }
        );

    private final List<String> schemes;
    private final List<String> hosts;
    private final List<Integer> ports;

    public URIGenerationOptions(
        String[] schemes,
        String[] hosts,
        int[] ports
    ) {
        if (schemes == null) {
            throw new IllegalArgumentException("The argument 'schemes' is null.");
        }

        if (schemes.length == 0) {
            throw new IllegalArgumentException("The argument 'schemes' is empty.");
        }

        for (String protocol : schemes) {
            if (protocol == null) {
                String message = "The argument 'schemes' contains null element.";
                throw new IllegalArgumentException(message);
            }
        }

        if (hosts == null) {
            throw new IllegalArgumentException("The argument 'hosts' is null.");
        }

        if (hosts.length == 0) {
            throw new IllegalArgumentException("The argument 'hosts' is empty.");
        }

        for (String host : hosts) {
            if (host == null) {
                String message = "The argument 'hosts' contains null element.";
                throw new IllegalArgumentException(message);
            }
        }

        if (ports == null) {
            throw new IllegalArgumentException("The argument 'ports' is null.");
        }

        for (int port : ports) {
            if (port < 0) {
                String message = "The argument 'ports' contains negative element.";
                throw new IllegalArgumentException(message);
            }
        }

        this.schemes = unmodifiableList(asList(schemes));
        this.hosts = unmodifiableList(asList(hosts));
        this.ports = unmodifiableList(stream(ports).boxed().collect(toList()));
    }

    public List<String> schemes() {
        return schemes;
    }

    public List<String> hosts() {
        return hosts;
    }

    public List<Integer> ports() {
        return ports;
    }
}
