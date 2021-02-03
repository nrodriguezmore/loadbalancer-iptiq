package com.iptiq.loadbalancer.strategy;

import com.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.iptiq.loadbalancer.providers.ProviderService;

public class RoundRobinStrategy implements Strategy {

    private Integer lastInvoked = 0;
    private final Object lock = new Object();

    public void execute(ProviderService providerService) {
        if (!providerService.providersAvailable()) {
            throw new NoProvidersAvailableException();
        }
        int current;
        synchronized (lock) {
            boolean found = false;
            while (!found) {
                lastInvoked = (lastInvoked + 1) % providerService.getProviderCount();
                found = providerService.isIncluded(lastInvoked);
            }
            current = lastInvoked;
        }
        providerService.invokeProvider(current);

    }

}
