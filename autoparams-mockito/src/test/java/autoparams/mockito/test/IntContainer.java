package autoparams.mockito.test;

public interface IntContainer {

    int getValue();

    default long square() {
        long value = getValue();
        return value * value;
    }
}
