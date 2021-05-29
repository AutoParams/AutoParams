package org.javaunit.autoparams.lombok.test;

import lombok.Builder;

@Builder
public class HasBuilder {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
