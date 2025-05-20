package autoparams;

/**
 * Converts an object to a different type suitable for a specific test method
 * parameter.
 * <p>
 * This interface defines the contract for converters that transform objects
 * from one type to another during test method parameter resolution. It is used
 * in the AutoParams framework to adapt supplied values to match the expected
 * types of test method parameters.
 * </p>
 *
 * @see DefaultAssetConverter
 */
@FunctionalInterface
public interface AssetConverter {

    /**
     * Converts an object to a type suitable for the specified test method
     * parameter.
     *
     * @param query information about the test method parameter being resolved
     * @param asset the object to be converted
     * @return the converted object that matches the parameter requirements
     */
    Object convert(ParameterQuery query, Object asset);
}
