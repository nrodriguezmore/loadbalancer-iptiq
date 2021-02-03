package com.iptiq.loadbalancer.exceptions;

public class InvalidProviderPositionException extends RuntimeException {

    private static final String message = "Invoked provider does not exist";

    public InvalidProviderPositionException() {
        super(message);
    }

}
