package com.iptiq.loadbalancer.strategy;

public class StrategyFactory {

    RoundRobinStrategy roundRobinStrategy = new RoundRobinStrategy();

    RandomStrategy randomStrategy = new RandomStrategy();

    public Strategy getStrategy(StrategyType strategyType) {

        switch (strategyType) {
            case ROUND_ROBIN:
                return roundRobinStrategy;
            case RANDOM:
                return randomStrategy;
            default:
                return roundRobinStrategy;
        }

    }
}
