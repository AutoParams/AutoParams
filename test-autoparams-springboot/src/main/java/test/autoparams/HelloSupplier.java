package test.autoparams;

import org.springframework.stereotype.Component;

@Component
public class HelloSupplier implements MessageSupplier {

    @Override
    public String getMessage(String name) {
        return "Hello, " + name + "!";
    }
}
