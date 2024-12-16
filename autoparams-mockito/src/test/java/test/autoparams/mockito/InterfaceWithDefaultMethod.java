package test.autoparams.mockito;

public interface InterfaceWithDefaultMethod {

    int getValue();

    default int getSquaredValue() {
        return getValue() * getValue();
    }
}
