package com.iptiq.loadbalancer.exceptions;

public class NoProvidersException extends RuntimeException {

    private static final String message = "There are no providers registered in the load balancer";

    public NoProvidersException() {
        super(message);
    }

}
