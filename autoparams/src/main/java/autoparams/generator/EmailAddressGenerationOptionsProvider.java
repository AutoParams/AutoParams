package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

public final class EmailAddressGenerationOptionsProvider
    extends ObjectGeneratorBase<EmailAddressGenerationOptions> {

    private final EmailAddressGenerationOptions options;

    public EmailAddressGenerationOptionsProvider(
        EmailAddressGenerationOptions options
    ) {
        this.options = options;
    }

    EmailAddressGenerationOptionsProvider() {
        this(EmailAddressGenerationOptions.DEFAULT);
    }

    @Override
    protected EmailAddressGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return options;
    }
}
