package autoparams.test;

public class HasMultipleConstructors {

    private final String value;

    public HasMultipleConstructors(String value) {
        this.value = value;
    }

    public HasMultipleConstructors() {
        this(null);
    }

    public String getValue() {
        return value;
    }
}
