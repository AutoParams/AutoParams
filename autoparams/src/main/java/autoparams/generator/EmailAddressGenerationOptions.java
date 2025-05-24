package autoparams.generator;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Represents options for generating email address strings.
 * <p>
 * This class allows specifying a list of domains that can be used when
 * generating email address strings. Instances of this class are immutable.
 * </p>
 *
 */
public final class EmailAddressGenerationOptions {

    /**
     * Provides default options for generating email address strings.
     * <p>
     * This instance is configured with a single default domain: "test.com".
     * </p>
     */
    public static final EmailAddressGenerationOptions DEFAULT =
        new EmailAddressGenerationOptions(new String[] { "test.com" });

    private final List<String> domains;

    /**
     * Constructs an instance of {@link EmailAddressGenerationOptions} with the
     * specified domains.
     *
     * @param domains an array of strings representing the list of domains that
     *                can be used when generating email address strings. This
     *                array must not be null or empty, and none of its elements
     *                can be null.
     * @throws IllegalArgumentException if {@code domains} is null, or if
     *                                  {@code domains} is empty, or if
     *                                  {@code domains} contains any null
     *                                  elements.
     */
    public EmailAddressGenerationOptions(String[] domains) {
        if (domains == null) {
            throw new IllegalArgumentException("The argument 'domains' is null.");
        }

        if (domains.length == 0) {
            throw new IllegalArgumentException("The argument 'domains' is empty.");
        }

        for (String domain : domains) {
            if (domain == null) {
                throw new IllegalArgumentException("The argument 'domains' contains null element.");
            }
        }

        this.domains = unmodifiableList(asList(domains));
    }

    /**
     * Gets the list of domains that can be used when generating email address
     * strings.
     *
     * @return an unmodifiable list of domain strings.
     */
    public List<String> domains() {
        return domains;
    }

    /**
     * Returns a string representation of this
     * {@link EmailAddressGenerationOptions} in the format
     * "EmailAddressGenerationOptions[domains=[...]]".
     *
     * @return a string representation of this
     *         {@link EmailAddressGenerationOptions}
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
            "EmailAddressGenerationOptions[domains=["
        );
        for (int i = 0; i < domains.size(); i++) {
            result.append("\"").append(domains.get(i)).append("\"");
            if (i < domains.size() - 1) {
                result.append(", ");
            }
        }

        result.append("]]");
        return result.toString();
    }
}
