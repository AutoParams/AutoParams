package test.autoparams;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DerivedClassWithInheritedSetter extends BaseClassWithSetter {

    private String derivedValue;
}
