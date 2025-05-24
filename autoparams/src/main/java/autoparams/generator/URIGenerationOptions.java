package autoparams.generator;

import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * Represents options for generating {@link URI} objects.
 * <p>
 * This class allows specifying lists of schemes, hosts, and ports that can be
 * used when generating {@link URI}s. Instances of this class are immutable.
 * </p>
 *
 * @see URI
 */
public final class URIGenerationOptions {

    /**
     * Provides default options for generating {@link URI} objects.
     * <p>
     * This instance is configured with the following default values:
     * </p>
     * <ul>
     *   <li>Schemes: "https"</li>
     *   <li>Hosts: "test.com"</li>
     *   <li>Ports: An empty list, meaning the default port for the scheme will
     *   be used.</li>
     * </ul>
     */
    public static final URIGenerationOptions DEFAULT =
        new URIGenerationOptions(
            new String[] { "https" },
            new String[] { "test.com" },
            new int[] { }
        );

    private final List<String> schemes;
    private final List<String> hosts;
    private final List<Integer> ports;

    /**
     * Constructs an instance of {@link URIGenerationOptions} with the
     * specified schemes, hosts, and ports.
     *
     * @param schemes an array of strings representing the URI schemes (e.g.,
     *                "http", "https"). This array must not be null or empty,
     *                and none of its elements can be null.
     * @param hosts   an array of strings representing the host names (e.g.,
     *                "example.com", "localhost"). This array must not be null
     *                or empty, and none of its elements can be null.
     * @param ports   an array of integers representing the port numbers. This
     *                array must not be null, and none of its elements can be
     *                negative. An empty array is allowed if no specific ports
     *                are to be used, allowing the default port for the scheme.
     * @throws IllegalArgumentException if {@code schemes} is null or empty, or
     *                                  contains null elements; or if
     *                                  {@code hosts} is null or empty, or
     *                                  contains null elements; or if
     *                                  {@code ports} is null or contains
     *                                  negative elements.
     */
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

    /**
     * Gets the list of URI schemes that can be used when generating
     * {@link URI}s.
     * <p>
     * This list is unmodifiable and contains the schemes provided when the
     * {@link URIGenerationOptions} instance was created.
     * </p>
     *
     * @return an unmodifiable list of URI scheme strings.
     */
    public List<String> schemes() {
        return schemes;
    }

    /**
     * Gets the list of host names that can be used when generating
     * {@link URI}s.
     * <p>
     * This list is unmodifiable and contains the host names provided when the
     * {@link URIGenerationOptions} instance was created.
     * </p>
     *
     * @return an unmodifiable list of host name strings.
     */
    public List<String> hosts() {
        return hosts;
    }

    /**
     * Gets the list of port numbers that can be used when generating
     * {@link URI}s.
     * <p>
     * This list is unmodifiable and contains the port numbers provided when the
     * {@link URIGenerationOptions} instance was created. An empty list
     * indicates that the default port for the selected scheme should be used.
     * </p>
     *
     * @return an unmodifiable list of port numbers.
     */
    public List<Integer> ports() {
        return ports;
    }

    /**
     * Returns a string representation of this {@link URIGenerationOptions} in
     * the format "URIGenerationOptions[schemes=..., hosts=..., ports=...]".
     *
     * @return a string representation of this {@link URIGenerationOptions}
     */
    @Override
    public String toString() {
        return "URIGenerationOptions["
            + "schemes=" + formatValues(schemes)
            + ", hosts=" + formatValues(hosts)
            + ", ports=" + ports + "]";
    }

    private static String formatValues(List<String> values) {
        StringBuilder result = new StringBuilder("[");
        int size = values.size();
        for (int i = 0; i < size; i++) {
            result.append("\"").append(values.get(i)).append("\"");
            if (i < size - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }
}
