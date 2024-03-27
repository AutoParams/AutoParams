package test.autoparams.lombok;

import java.util.List;

import lombok.Builder;

@Builder
public class HasBuilder {

    private Long id;
    private String name;
    private List<String> tags;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }
}
