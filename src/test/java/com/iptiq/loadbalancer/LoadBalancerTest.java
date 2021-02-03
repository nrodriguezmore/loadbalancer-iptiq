package com.iptiq.loadbalancer;

import com.iptiq.loadbalancer.providers.Provider;
import com.iptiq.loadbalancer.providers.ProviderService;
import com.iptiq.loadbalancer.strategy.Strategy;
import com.iptiq.loadbalancer.strategy.StrategyFactory;
import com.iptiq.loadbalancer.strategy.StrategyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoadBalancerTest {

    @InjectMocks
    private LoadBalancer loadBalancer;

    @Spy
    ProviderService providerService;
    @Spy
    StrategyFactory strategyFactory;

    @Test
    public void register() {
        //given
        List<Provider> providers = new ArrayList<>();

        //when
        loadBalancer.register(providers);

        //then
        verify(providerService, times(1)).setProviders(providers);

    }

    @Test
    public void schedulerExecutesOnAllProviders() {

        //given
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider());

        //when
        loadBalancer.register(providers);


        //then
        verify(providerService, times(1)).checkProviders();
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            assert(false);
        }
        verify(providerService, times(2)).checkProviders();

    }



    @ParameterizedTest
    @MethodSource("getParamenters")
    public void get(StrategyType strategyType) {
        //given
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider());
        LoadBalancer loadBalancer = spy(this.loadBalancer);
        loadBalancer.register(providers);
        Strategy strategy = Mockito.spy(strategyFactory.getStrategy(strategyType));

        doReturn(strategy).when(loadBalancer).getStrategy(strategyType);

        //when
        loadBalancer.get(strategyType);

        //then
        verify(strategy, times(1)).execute(any());
    }

    @Test
    public void includes() {
        //given
        List<Provider> providers = new ArrayList<>();
        Provider provider = new Provider();
        providers.add(provider);
        loadBalancer.register(providers);

        //when
        loadBalancer.include(provider.getInstanceId());

        //then
        verify(providerService, times(1)).include(provider.getInstanceId());

    }

    @Test
    public void excludes() {
        //given
        List<Provider> providers = new ArrayList<>();
        Provider provider = new Provider();
        providers.add(provider);
        loadBalancer.register(providers);

        //when
        loadBalancer.exclude(provider.getInstanceId());

        //then
        verify(providerService, times(1)).exclude(provider.getInstanceId());
    }

    private static Stream<Arguments> getParamenters() {
        return Stream.of(
                Arguments.of(StrategyType.RANDOM),
                Arguments.of(StrategyType.ROUND_ROBIN)
        );
    }

}
