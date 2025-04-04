package autoparams;

public final class DefaultAssetConverter implements AssetConverter {

    private final AssetConverter converter = new StringAssetConverter();

    @Override
    public Object convert(ParameterQuery query, Object asset) {
        return converter.convert(query, asset);
    }
}
