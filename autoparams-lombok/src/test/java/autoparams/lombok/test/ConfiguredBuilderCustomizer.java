package autoparams.lombok.test;

import autoparams.lombok.BuilderCustomizer;

public class ConfiguredBuilderCustomizer extends BuilderCustomizer {
    public ConfiguredBuilderCustomizer() {
        super("getBuilder", "create");
    }
}
