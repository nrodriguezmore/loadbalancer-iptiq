package com.iptiq.loadbalancer.strategy;

import com.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.iptiq.loadbalancer.providers.ProviderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RandomStrategyTest {

    @Test
    public void executeFailsNoProvidersAvailable() {

        //given
        RandomStrategy strategy = new RandomStrategy();
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
        RandomStrategy strategy = new RandomStrategy();
        RandomStrategy strategySpy = spy(strategy);
        ProviderService providerService = mock(ProviderService.class);

        when(providerService.getProviderCount()).thenReturn(8);
        when(providerService.providersAvailable()).thenReturn(true);

        doReturn(true).doCallRealMethod().when(providerService).isIncluded(anyInt());
        doReturn(5).when(strategySpy).getNextRandomValue(anyInt());

        //when

        strategySpy.execute(providerService);

        //then
        verify(providerService, times(1)).invokeProvider(anyInt());
        verify(providerService, times(1)).isIncluded(anyInt());
    }

    @Test
    public void executeSucceedsSecondAttempt() {

        //given
        RandomStrategy strategy = new RandomStrategy();
        RandomStrategy strategySpy = spy(strategy);
        ProviderService providerService = mock(ProviderService.class);

        when(providerService.getProviderCount()).thenReturn(8);
        when(providerService.providersAvailable()).thenReturn(true);

        doReturn(false).doReturn(true).doCallRealMethod().when(providerService).isIncluded(anyInt());
        doReturn(5).when(strategySpy).getNextRandomValue(anyInt());

        //when

        strategySpy.execute(providerService);

        //then
        verify(providerService, times(1)).invokeProvider(anyInt());
        verify(providerService, times(2)).isIncluded(anyInt());
    }


}
