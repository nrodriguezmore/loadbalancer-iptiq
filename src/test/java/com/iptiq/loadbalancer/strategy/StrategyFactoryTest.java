package com.iptiq.loadbalancer.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class StrategyFactoryTest {

    @ParameterizedTest
    @MethodSource("getStrategyArguments")
    public void getStrategy(Class<?> expectedStrategy, StrategyType strategyType) {

        //given
        StrategyFactory strategyFactory = new StrategyFactory();

        //when
        Strategy strategy = strategyFactory.getStrategy(strategyType);

        //then
        Assertions.assertEquals(expectedStrategy, strategy.getClass());
    }


    private static Stream<Arguments> getStrategyArguments() {
        return Stream.of(
                Arguments.of(RandomStrategy.class, StrategyType.RANDOM),
                Arguments.of(RoundRobinStrategy.class, StrategyType.ROUND_ROBIN)
        );
    }
}
