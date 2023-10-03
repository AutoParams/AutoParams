package test.autoparams.mockito;

public abstract class IntContainer {

    public abstract int getValue();

    public long square() {
        long value = getValue();
        return value * value;
    }
}
