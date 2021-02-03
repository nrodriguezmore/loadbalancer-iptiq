package com.iptiq.loadbalancer;

import com.iptiq.loadbalancer.exceptions.FailedToObtainProviderException;
import com.iptiq.loadbalancer.providers.Provider;
import com.iptiq.loadbalancer.providers.ProviderService;
import com.iptiq.loadbalancer.strategy.Strategy;
import com.iptiq.loadbalancer.strategy.StrategyFactory;
import com.iptiq.loadbalancer.strategy.StrategyType;
import com.iptiq.loadbalancer.tasks.ProviderCheckerTask;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

public class LoadBalancer {

    private static final Integer PROVIDER_VERIFICATION_INTERVAL = 10000;
    private static final Integer Y_REQUEST_PER_PROVIDER = 5;
    ProviderService providerService;

    StrategyFactory strategyFactory;

    private Timer scheduler;

    private Semaphore maxConcurrentThreads;


    public LoadBalancer() {
        this.strategyFactory = new StrategyFactory();
        this.providerService = new ProviderService();
    }

    /**
     *
     * @param providers list of providers to register
     * Registers providers as the ones avaialble for using within the lead balancer
     */
    public void register(List<Provider> providers) {
        //when we register the providers, we need to set how many concurrent threads can come in
        providerService.setProviders(providers);

        //clear up previous timer task, so tasks are not hang in there
        if (scheduler != null) {
            scheduler.cancel();
        }
        //this allows to check every 10 seconds whether providers are still available
        scheduler = new Timer();
        scheduler.scheduleAtFixedRate(new ProviderCheckerTask(providerService), PROVIDER_VERIFICATION_INTERVAL, PROVIDER_VERIFICATION_INTERVAL);

        maxConcurrentThreads = new Semaphore(providers.size() * Y_REQUEST_PER_PROVIDER);

    }

    /**
     *
     * @param strategyType strategy desired to define which provider should the load balancer invoke
     * Invokes the get() method on the corresponding provider, according to given strategy
     */
    public void get(StrategyType strategyType) {
        Strategy selectedStrategy = getStrategy(strategyType);
        try {
            maxConcurrentThreads.acquire(); //we only allow a certain amount of threads to execute a strategy at the same time
            selectedStrategy.execute(providerService);
        } catch (InterruptedException ex) {
            throw new FailedToObtainProviderException();
        } finally {
            maxConcurrentThreads.release(); //release, else noone can access after a while
        }
    }

    protected Strategy getStrategy(StrategyType strategyType) {
        return strategyFactory.getStrategy(strategyType);
    }

    public void include(String providerId) {
        providerService.include(providerId);
    }

    public void exclude(String providerId) {
        providerService.exclude(providerId);
    }




}
