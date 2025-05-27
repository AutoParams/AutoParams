package test.autoparams;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;
import test.autoparams.animal.Animal;
import test.autoparams.animal.Bird;
import test.autoparams.animal.Fish;
import test.autoparams.payment.CreditCardPayment;
import test.autoparams.payment.MobilePayment;
import test.autoparams.payment.PayPalPayment;
import test.autoparams.payment.Payment;
import test.autoparams.shape.Circle;
import test.autoparams.shape.Rectangle;
import test.autoparams.shape.Shape;
import test.autoparams.shape.Square;
import test.autoparams.vehicle.Car;
import test.autoparams.vehicle.Motorcycle;
import test.autoparams.vehicle.Ship;
import test.autoparams.vehicle.Submarine;
import test.autoparams.vehicle.Vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SpecsForSealedTypes {

    @Test
    @AutoParams
    void sut_creates_an_instance_of_a_permitted_subclass_of_sealed_class(
        ResolutionContext context
    ) {
        assertThatCode(() -> {
            Shape shape = context.resolve();
            assertThat(shape).isInstanceOfAny(
                Circle.class,
                Rectangle.class,
                Square.class
            );
        }).doesNotThrowAnyException();
    }

    @Test
    @AutoParams
    void sut_creates_an_instance_of_a_permitted_subclass_of_a_nested_sealed_hierarchy(
        ResolutionContext context
    ) {
        assertThatCode(() -> {
            Vehicle vehicle = context.resolve();
            assertThat(vehicle).isInstanceOfAny(
                Car.class,
                Motorcycle.class,
                Ship.class,
                Submarine.class
            );
        }).doesNotThrowAnyException();
    }

    @Test
    @AutoParams
    void sut_excludes_abstract_permitted_subclasses_when_creating_instances(
        ResolutionContext context
    ) {
        assertThatCode(() -> {
            Animal animal = context.resolve();
            assertThat(animal).isInstanceOfAny(Bird.class, Fish.class);
        }).doesNotThrowAnyException();
    }

    @Test
    @AutoParams
    void sut_creates_an_instance_of_a_class_implementing_sealed_interface(
        ResolutionContext context
    ) {
        assertThatCode(() -> {
            Payment payment = context.resolve();
            assertThat(payment).isInstanceOfAny(
                CreditCardPayment.class,
                MobilePayment.class,
                PayPalPayment.class
            );
        }).doesNotThrowAnyException();
    }
}
