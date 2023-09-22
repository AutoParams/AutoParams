package test.autoparams.mockito;

public interface IntContainer {

    int getValue();

    default long square() {
        long value = getValue();
        return value * value;
    }
}
