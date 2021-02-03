package com.iptiq.loadbalancer.strategy;

import com.iptiq.loadbalancer.providers.ProviderService;

public interface Strategy {

    void execute(ProviderService providerService);
}
