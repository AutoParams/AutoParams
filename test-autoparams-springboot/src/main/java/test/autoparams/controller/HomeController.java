package test.autoparams.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.autoparams.MessageSupplier;

@RestController
public class HomeController {

    private final MessageSupplier messageSupplier;

    public HomeController(MessageSupplier messageSupplier) {
        this.messageSupplier = messageSupplier;
    }

    public record MessageCarrier(String message) {
    }

    @GetMapping("/")
    public MessageCarrier index(@RequestParam String name) {
        return new MessageCarrier(messageSupplier.getMessage(name));
    }
}
