package com.iptiq.loadbalancer.exceptions;

public class NoProvidersAvailableException extends RuntimeException {

    private static final String message = "There are no providers available to execute your request at this moment";

    public NoProvidersAvailableException() {
        super(message);
    }

}
