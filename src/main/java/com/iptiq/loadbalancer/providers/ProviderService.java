package com.iptiq.loadbalancer.providers;

import com.iptiq.loadbalancer.exceptions.InvalidProviderPositionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderService {

    private static int MAX_PROVIDERS_SIZE = 10;
    private static int EXCLUDED = 0;
    private static int CANDIDATE_TO_RE_INCLUDE = 1;
    private static int INCLUDED = 2;

    private List<Provider> providers;
    private ConcurrentHashMap<String, Integer> includedProviders;

    public ProviderService() {
        this.providers = new ArrayList<>();
        this.includedProviders = new ConcurrentHashMap<>();
    }

    public List<Provider> getProviders() {
        return this.providers;
    }

    public void setProviders(List<Provider> providers) {
        if (providers.size() > MAX_PROVIDERS_SIZE) {
            this.providers = providers.subList(0, MAX_PROVIDERS_SIZE);
        } else {
            this.providers = providers;
        }
        this.includedProviders = new ConcurrentHashMap<>();
        checkProviders();
    }

    public void include(String providerId) {
        if (includedProviders.containsKey(providerId)) {
            includedProviders.put(providerId, INCLUDED);
        }
    }

    public void exclude(String providerId) {
        if (includedProviders.containsKey(providerId)) {
            includedProviders.put(providerId, EXCLUDED);
        }
    }

    public void checkProviders() {
        providers.parallelStream().forEach(provider -> {
            String providerId = provider.getInstanceId();
            Boolean isAlive = provider.check();
            int nextStatus;
            if (! this.includedProviders.containsKey(providerId)) {
                //new provider to add
                nextStatus = isAlive ? INCLUDED : EXCLUDED;
            } else if (!isAlive) {
                nextStatus = EXCLUDED;
            } else {
                int currentStatus = this.includedProviders.get(providerId);
                nextStatus = currentStatus == EXCLUDED ? CANDIDATE_TO_RE_INCLUDE : INCLUDED;

            }
            this.includedProviders.put(provider.getInstanceId(), nextStatus);
        });
    }

    public void invokeProvider(int position) {
        if (position < 0 || position >= providers.size()) {
            throw new InvalidProviderPositionException();
        }
        providers.get(position).get();
    }

    public int getProviderCount() {
        return this.providers.size();
    }

    public Boolean isIncluded(int position) {
        if (position >= providers.size()) {
            return false;
        }
        String key = this.providers.get(position).getInstanceId();
        return this.includedProviders.get(key) == INCLUDED;
    }

    public Boolean providersAvailable() {
        return this.includedProviders.values().parallelStream().anyMatch(n -> n == INCLUDED);
    }


}
