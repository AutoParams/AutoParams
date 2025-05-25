package test.autoparams;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BooleanBag {

    private final boolean value;

    public boolean getValue() {
        return value;
    }
}
