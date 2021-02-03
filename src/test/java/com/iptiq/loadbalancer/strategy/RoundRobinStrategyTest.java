package com.iptiq.loadbalancer.strategy;

import com.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.iptiq.loadbalancer.providers.ProviderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoundRobinStrategyTest {

    @Test
    public void executeFailsNoProvidersAvailable() {

        //given
        RoundRobinStrategy strategy = new RoundRobinStrategy();
        ProviderService providerService = mock(ProviderService.class);
        when(providerService.providersAvailable()).thenReturn(false);

        //when
        //then
        Assertions.assertThrows(NoProvidersAvailableException.class,
                () -> strategy.execute(providerService)
        );
    }

    @Test
    public void executeSucceedsFirstAttempt() {
        //given
        RoundRobinStrategy strategy = new RoundRobinStrategy();
        RoundRobinStrategy strategySpy = spy(strategy);
        ProviderService providerService = mock(ProviderService.class);

        when(providerService.getProviderCount()).thenReturn(8);
        when(providerService.providersAvailable()).thenReturn(true);

        doReturn(true).doCallRealMethod().when(providerService).isIncluded(anyInt());

        //when

        strategySpy.execute(providerService);

        //then
        verify(providerService, times(1)).invokeProvider(1);
        verify(providerService, times(1)).isIncluded(anyInt());
    }

    @Test
    public void executeSucceedsSecondAttempt() {

        //given
        RoundRobinStrategy strategy = new RoundRobinStrategy();
        RoundRobinStrategy strategySpy = spy(strategy);
        ProviderService providerService = mock(ProviderService.class);

        when(providerService.getProviderCount()).thenReturn(8);
        when(providerService.providersAvailable()).thenReturn(true);

        doReturn(false).doReturn(true).doCallRealMethod().when(providerService).isIncluded(anyInt());

        //when

        strategySpy.execute(providerService);

        //then
        verify(providerService, times(1)).invokeProvider(2);
        verify(providerService, times(2)).isIncluded(anyInt());
    }


}
