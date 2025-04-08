package test.autoparams.customization;

import lombok.Getter;

@Getter
public class Entity<T> extends Versioned {

    private T id;
}
