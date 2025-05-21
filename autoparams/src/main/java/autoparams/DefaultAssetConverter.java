package autoparams;

/**
 * A default implementation of {@link AssetConverter} that converts objects
 * for test method parameters.
 * <p>
 * This class serves as the primary implementation of the {@link AssetConverter}
 * interface in AutoParams.
 * </p>
 *
 * @see AssetConverter
 */
public final class DefaultAssetConverter implements AssetConverter {

    private final AssetConverter converter = new StringAssetConverter();

    /**
     * Creates an instance of {@link DefaultAssetConverter}.
     */
    public DefaultAssetConverter() {
    }

    /**
     * Converts an object to a type suitable for the specified test method
     * parameter.
     *
     * @param query information about the test method parameter being resolved
     * @param asset the object to be converted
     * @return the converted object that matches the parameter requirements
     */
    @Override
    public Object convert(ParameterQuery query, Object asset) {
        return converter.convert(query, asset);
    }
}
