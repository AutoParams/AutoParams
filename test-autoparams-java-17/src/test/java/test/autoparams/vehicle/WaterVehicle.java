package test.autoparams.vehicle;

public abstract sealed class WaterVehicle
    extends Vehicle
    permits Ship, Submarine {
}
