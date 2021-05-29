package org.javaunit.autoparams.lombok.test;

import lombok.Builder;

@Builder(builderMethodName = "getBuilder", buildMethodName = "create")
public class HasConfiguredBuilder {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
