package com.iptiq.loadbalancer.providers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class ProviderServiceTest {

    private ProviderService providerService = new ProviderService();

    @ParameterizedTest
    @MethodSource("setProvidersArguments")
    public void setProvidersAndGetCount(int providersRequestedSize, int expectedProvidersSize) {

        //given
        providerService.setProviders(getDummyProviders(providersRequestedSize));

        //when
        int count = providerService.getProviderCount();

        //then
        Assertions.assertEquals(expectedProvidersSize, count);
    }

    @Test
    public void includeAndExclude_1() {
        //given
        List<Provider> providers = getDummyProviders(8);
        providerService.setProviders(providers);
        int toExclude = 4;
        String providerId = providers.get(toExclude).getInstanceId();

        //when
        providerService.exclude(providerId);

        //then
        Assertions.assertFalse(providerService.isIncluded(toExclude));

    }

    @Test
    public void includeAndExclude_2() {
        //given
        List<Provider> providers = getDummyProviders(8);
        providerService.setProviders(providers);
        int toExclude = 4;
        String providerId = providers.get(toExclude).getInstanceId();

        //when
        providerService.exclude(providerId);
        providerService.include(providerId);

        //then
        Assertions.assertTrue(providerService.isIncluded(toExclude));
    }


    @Test
    public void providersAvailable_false() {
        //given
        List<Provider> providers = getDummyProviders(8);
        providerService.setProviders(providers);

        //when
        for (Provider provider: providers) {
            providerService.exclude(provider.getInstanceId());
        }

        //then
        Assertions.assertFalse(providerService.providersAvailable());
    }

    @Test
    public void providersAvailable_true() {
        //given
        List<Provider> providers = getDummyProviders(8);
        providerService.setProviders(providers);

        //when
        for (Provider provider: providers) {
            providerService.exclude(provider.getInstanceId());
        }
        providerService.include(providers.get(0).getInstanceId());

        //then
        Assertions.assertTrue(providerService.providersAvailable());
    }


    @Test
    public void providersAvailable_true2() {
        //given
        List<Provider> providers = getDummyProviders(8);
        providerService.setProviders(providers);

        //when

        //then
        Assertions.assertTrue(providerService.providersAvailable());
    }

    @Test
    public void invokeProvider() {

        //given
        List<Provider> providers = getMockedProviders(8);
        providerService.setProviders(providers);
        int position = 3;
        Provider provider = providers.get(position);

        //when
        providerService.invokeProvider(position);

        //then
        //we want to verify that only one provider is invoked with the get method, the corrsponding one
        Mockito.verify(provider, Mockito.times(1)).get();
        //we also verify that the other providers were not invoked
        for (int i = 0; i < providers.size(); i++) {
            if (i != position) {
                Mockito.verify(providers.get(i), Mockito.times(0)).get();
            }
        }

    }

    private static Stream<Arguments> setProvidersArguments() {
        return Stream.of(
                Arguments.of(5, 5),
                Arguments.of(0, 0),
                Arguments.of(10, 10),
                Arguments.of(11, 10),
                Arguments.of(100, 10)
        );
    }

    /**
     *
     * @param count amount of providers to create
     * @return returns a dummy list of providers of size count
     *
     */
    private List<Provider> getDummyProviders(int count) {
        if (count == 0) {
            return new ArrayList<>();
        }
        return IntStream.range(0, count).mapToObj(value -> new Provider()).collect(Collectors.toList());
    }

    /**
     *
     * @param count amount of mocked providers to create
     * @return returns a list of mocked providers of size count
     *
     */
    private List<Provider> getMockedProviders(int count) {
        if (count == 0) {
            return new ArrayList<>();
        }
        return IntStream.range(0, count).mapToObj(value -> {
            Provider provider = Mockito.mock(Provider.class);
            when(provider.getInstanceId()).thenReturn(String.valueOf(value));
            when(provider.check()).thenReturn(true);
            return provider;
        }).collect(Collectors.toList());
    }
}
