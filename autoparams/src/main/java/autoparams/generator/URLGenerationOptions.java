package autoparams.generator;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class URLGenerationOptions {

    public static final URLGenerationOptions DEFAULT =
        new URLGenerationOptions(
            new String[] { "https" },
            new String[] { "test.com" },
            new int[] { }
        );

    private final List<String> protocols;
    private final List<String> hosts;
    private final List<Integer> ports;

    public URLGenerationOptions(
        String[] protocols,
        String[] hosts,
        int[] ports
    ) {
        if (protocols == null) {
            throw new IllegalArgumentException("The argument 'protocols' is null.");
        }

        if (protocols.length == 0) {
            throw new IllegalArgumentException("The argument 'protocols' is empty.");
        }

        for (String protocol : protocols) {
            if (protocol == null) {
                String message = "The argument 'protocols' contains null element.";
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

        this.protocols = unmodifiableList(asList(protocols));
        this.hosts = unmodifiableList(asList(hosts));
        this.ports = unmodifiableList(stream(ports).boxed().collect(toList()));
    }

    public List<String> protocols() {
        return protocols;
    }

    public List<String> hosts() {
        return hosts;
    }

    public List<Integer> ports() {
        return ports;
    }
}
