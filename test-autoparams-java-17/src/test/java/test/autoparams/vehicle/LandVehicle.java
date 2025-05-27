package test.autoparams.vehicle;

public abstract sealed class LandVehicle
    extends Vehicle
    permits Car, Motorcycle {
}
