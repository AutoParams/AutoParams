package test.autoparams;

public class HelloSupplier implements MessageSupplier {

    @Override
    public String getMessage(String name) {
        return "Hello, " + name + "!";
    }
}
