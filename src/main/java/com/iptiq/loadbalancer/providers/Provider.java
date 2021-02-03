package com.iptiq.loadbalancer.providers;

import java.util.UUID;

public class Provider {

    private String instanceId;

    public Provider() {
        this.instanceId = UUID.randomUUID().toString();
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public String get() {
        System.out.println(this.instanceId);
        return this.instanceId;
    }

    public Boolean check() {

        //TODO: This should be a real implementation to check if the provider is alive or not
        return true;
    }
}
