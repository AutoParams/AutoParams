package autoparams.generator;

import java.net.URI;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

/**
 * Provides instances of {@link URIGenerationOptions}.
 * <p>
 * This provider is responsible for supplying {@link URIGenerationOptions} to
 * the AutoParams framework. AutoParams typically retrieves these options via
 * the {@link ResolutionContext} when an instance of
 * {@link URIGenerationOptions} is required for generating {@link URI} objects.
 * </p>
 * <p>
 * By default, this provider supplies {@link URIGenerationOptions#DEFAULT}.
 * Users can customize the schemes, hosts, and ports used for generating
 * {@link URI} objects by creating an instance of this provider with
 * specific {@link URIGenerationOptions} and registering it as a custom
 * {@link ObjectGenerator}. This allows for tailored URI component
 * configurations.
 * </p>
 *
 * @see URIGenerationOptions
 * @see ObjectGeneratorBase
 * @see ResolutionContext
 */
public final class URIGenerationOptionsProvider
    extends ObjectGeneratorBase<URIGenerationOptions> {

    private final URIGenerationOptions options;

    /**
     * Constructs an instance of {@link URIGenerationOptionsProvider} with the
     * specified {@link URIGenerationOptions}.
     *
     * @param options the {@link URIGenerationOptions} to be provided by this
     *                provider.
     * @see URIGenerationOptions
     */
    public URIGenerationOptionsProvider(URIGenerationOptions options) {
        this.options = options;
    }

    URIGenerationOptionsProvider() {
        this(URIGenerationOptions.DEFAULT);
    }

    /**
     * Provides an instance of {@link URIGenerationOptions}.
     * <p>
     * This method returns the {@link URIGenerationOptions} instance that this
     * provider was configured with.
     * </p>
     *
     * @param query   the {@link ObjectQuery} for which the options are being
     *                generated. This parameter is not directly used in this
     *                implementation as the options are pre-configured.
     * @param context the {@link ResolutionContext} providing services and
     *                information for value generation. This parameter is not
     *                directly used in this implementation.
     * @return the configured {@link URIGenerationOptions} instance.
     * @see URIGenerationOptions
     */
    @Override
    protected URIGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return options;
    }
}
