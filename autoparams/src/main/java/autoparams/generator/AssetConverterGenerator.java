package autoparams.generator;

import autoparams.AssetConverter;
import autoparams.DefaultAssetConverter;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

class AssetConverterGenerator extends ObjectGeneratorBase<AssetConverter> {

    private final AssetConverter instance = new DefaultAssetConverter();

    @Override
    protected AssetConverter generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return instance;
    }
}
