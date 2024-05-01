package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class DefaultEmailAddressGenerationOptionsProvider
    extends ObjectGeneratorBase<EmailAddressGenerationOptions> {

    @Override
    protected EmailAddressGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return EmailAddressGenerationOptions.DEFAULT;
    }
}
