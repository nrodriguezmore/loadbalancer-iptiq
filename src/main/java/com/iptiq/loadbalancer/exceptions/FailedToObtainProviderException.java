package com.iptiq.loadbalancer.exceptions;

public class FailedToObtainProviderException extends RuntimeException {

    private static final String message = "There was a failure trying to obtain a provider";

    public FailedToObtainProviderException() {
        super(message);
    }

}
