package com.iptiq.loadbalancer.strategy;

import com.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.iptiq.loadbalancer.providers.ProviderService;

import java.util.Random;

public class RandomStrategy implements Strategy {

    public void execute(ProviderService providerService) {

        if (!providerService.providersAvailable()) {
            throw new NoProvidersAvailableException();
        }
        int position = -1;
        boolean found = false;
        while (!found) {
            position = getNextRandomValue(providerService.getProviderCount());
            found = providerService.isIncluded(position);
        }
        providerService.invokeProvider(position);

    }

    //we isolate randomisation, so we can mock it on tests
    int getNextRandomValue(int providerCount) {
        return new Random().nextInt(providerCount);
    }
}
