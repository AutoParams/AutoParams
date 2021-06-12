package org.javaunit.autoparams.customization.test;

public class ServiceClient {

    private final ServiceA serviceA;
    private final ServiceB serviceB;

    public ServiceClient(ServiceA serviceA, ServiceB serviceB) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }

    public ServiceA getServiceA() {
        return serviceA;
    }

    public ServiceB getServiceB() {
        return serviceB;
    }

}
