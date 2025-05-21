package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

/**
 * Provides instances of {@link EmailAddressGenerationOptions}.
 * <p>
 * This generator is responsible for providing
 * {@link EmailAddressGenerationOptions} to the AutoParams framework. AutoParams
 * typically retrieves these options via the {@link ResolutionContext} when an
 * instance of {@link EmailAddressGenerationOptions} is required for generating
 * email address strings.
 * </p>
 * <p>
 * By default, this provider supplies
 * {@link EmailAddressGenerationOptions#DEFAULT}. Users can customize the
 * domains used for generating email address strings by creating an instance of
 * this provider with specific {@link EmailAddressGenerationOptions} and
 * registering it as a custom {@link ObjectGenerator} within the AutoParams
 * framework. This allows for tailored email domain configurations.
 * </p>
 *
 * @see EmailAddressGenerationOptions
 * @see ObjectGeneratorBase
 * @see ResolutionContext
 */
public final class EmailAddressGenerationOptionsProvider
    extends ObjectGeneratorBase<EmailAddressGenerationOptions> {

    private final EmailAddressGenerationOptions options;

    /**
     * Constructs an instance of {@link EmailAddressGenerationOptionsProvider}
     * with the specified {@link EmailAddressGenerationOptions}.
     *
     * @param options the {@link EmailAddressGenerationOptions} to be provided
     *                by this generator.
     * @see EmailAddressGenerationOptions
     */
    public EmailAddressGenerationOptionsProvider(
        EmailAddressGenerationOptions options
    ) {
        this.options = options;
    }

    EmailAddressGenerationOptionsProvider() {
        this(EmailAddressGenerationOptions.DEFAULT);
    }

    /**
     * Provides an instance of {@link EmailAddressGenerationOptions}.
     * <p>
     * This method returns the {@link EmailAddressGenerationOptions} instance
     * that this provider was configured with.
     * </p>
     *
     * @param query   the {@link ObjectQuery} for which the options are being
     *                generated. This parameter is not directly used in this
     *                implementation as the options are pre-configured.
     * @param context the {@link ResolutionContext} providing services and
     *                information for value generation. This parameter is not
     *                directly used in this implementation.
     * @return the configured {@link EmailAddressGenerationOptions} instance.
     * @see EmailAddressGenerationOptions
     */
    @Override
    protected EmailAddressGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return options;
    }
}
