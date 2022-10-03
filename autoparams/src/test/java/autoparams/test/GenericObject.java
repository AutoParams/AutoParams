package autoparams.test;

public class GenericObject<T1, T2> {

    private final int value1;
    private final T1 value2;
    private final T2 value3;

    public GenericObject(int value1, T1 value2, T2 value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public int getValue1() {
        return value1;
    }

    public T1 getValue2() {
        return value2;
    }

    public T2 getValue3() {
        return value3;
    }

}
