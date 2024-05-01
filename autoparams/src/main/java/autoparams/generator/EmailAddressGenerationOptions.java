package autoparams.generator;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class EmailAddressGenerationOptions {

    public static final EmailAddressGenerationOptions DEFAULT =
        new EmailAddressGenerationOptions(new String[] { "test.com" });

    private final List<String> domains;

    public EmailAddressGenerationOptions(String[] domains) {
        if (domains == null) {
            throw new IllegalArgumentException("The argument 'domains' is null.");
        }

        for (String domain : domains) {
            if (domain == null) {
                throw new IllegalArgumentException("The argument 'domains' contains null element.");
            }
        }

        this.domains = unmodifiableList(asList(domains));
    }

    public List<String> domains() {
        return domains;
    }
}
