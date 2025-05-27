package test.autoparams.payment;

public sealed interface Payment
    permits CreditCardPayment, MobilePayment, PayPalPayment {
}
