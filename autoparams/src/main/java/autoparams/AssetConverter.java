package autoparams;

@FunctionalInterface
public interface AssetConverter {

    Object convert(ParameterQuery query, Object asset);
}
