package com.iptiq.loadbalancer.tasks;

import com.iptiq.loadbalancer.providers.ProviderService;

import java.util.TimerTask;

public class ProviderCheckerTask extends TimerTask {

    private ProviderService providerService;

    public ProviderCheckerTask(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public void run() {
        providerService.checkProviders();
    }
}
