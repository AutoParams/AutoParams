package org.javaunit.autoparams.lombok.test;

import org.javaunit.autoparams.lombok.BuilderCustomizer;

public class ConfiguredBuilderCustomizer extends BuilderCustomizer {
    public ConfiguredBuilderCustomizer() {
        super("getBuilder", "create");
    }
}
