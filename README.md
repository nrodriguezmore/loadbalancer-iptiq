# loadbalancer-iptiq


## Introduction  
This project is intended to implement a basic non production ready load balancer, according to the specifications of the exercise.

## Scope limitations
The scope of the project is reduced to what is requested on the assignment, plus a basic suite of unit tests.

## Requirements check
This load balancer allows:
- Registering a list of providers [here](https://github.com/nrodriguezmore/loadbalancer-iptiq/blob/main/src/main/java/com/iptiq/loadbalancer/LoadBalancer.java#L38).
- Select between two different strategies (random & round robin), with possibility to easily extend the strategy support and add a new one [here](https://github.com/nrodriguezmore/loadbalancer-iptiq/blob/0fd996cfb8e9f3d79bc35bc700d2af1ccf50da50/src/main/java/com/iptiq/loadbalancer/strategy/StrategyFactory.java#L11).
- Manually exclude/include nodes [here](https://github.com/nrodriguezmore/loadbalancer-iptiq/blob/main/src/main/java/com/iptiq/loadbalancer/LoadBalancer.java#L75-L81).
- Usage of Timer class to schedule a validation on every node, every X seconds [here](https://github.com/nrodriguezmore/loadbalancer-iptiq/blob/main/src/main/java/com/iptiq/loadbalancer/LoadBalancer.java#L48).
- Limit cluster capacity by using Semaphores [here](https://github.com/nrodriguezmore/loadbalancer-iptiq/blob/main/src/main/java/com/iptiq/loadbalancer/LoadBalancer.java#L62).

## Tools

For simplicity, Maven was used as compilation, dependency management and test suite runner tool. For running tests, execute on command line the command: 
`mvn test`
You can find out more about Maven [here](https://maven.apache.org/).

## Tests

There is a basic set of unit tests for the project, focused on strictly validating method behaviour within each class.
In the future, it would be ideal to add an integration test on the LoadBalancer class in order to test the entire functionality for the different use cases.

## Future Work

This load balancer does not take into account real production scenarios (of course it does not). However, to make it more robust we should add:
- Better suite of tests (ie: concurrency testing).
- Manage the fact that the check method inside a provider could take a lot of time to execute, and configure a timeout.
- Right now, we verify each node every X amount of seconds, no matter how long the previous verification took. Evaluate substituting scheduleAtFixedRate usage for schedule on invoking Timer class.
