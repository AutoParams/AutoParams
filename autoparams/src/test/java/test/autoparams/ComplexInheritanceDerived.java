package test.autoparams;

public class ComplexInheritanceDerived extends ComplexInheritanceMiddle {
    private String derivedProperty;

    public String getDerivedProperty() {
        return derivedProperty;
    }

    public void setDerivedProperty(String derivedProperty) {
        this.derivedProperty = derivedProperty;
    }
}
