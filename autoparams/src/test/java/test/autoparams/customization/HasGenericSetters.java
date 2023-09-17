package test.autoparams.customization;

public class HasGenericSetters<T1, T2> {
    private int value1;
    private T1 value2;
    private T2 value3;

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public T1 getValue2() {
        return value2;
    }

    public void setValue2(T1 value2) {
        this.value2 = value2;
    }

    public T2 getValue3() {
        return value3;
    }

    public void setValue3(T2 value3) {
        this.value3 = value3;
    }
}
