package fpt.tuanhm43.core.strategies;

import java.util.Map;

public class DiscountStrategyFactory {

    private final Map<String, BaseDiscountStrategy> strategies;

    public DiscountStrategyFactory(Map<String, BaseDiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public BaseDiscountStrategy getStrategy(String category) {
        return strategies.getOrDefault(category, new NoDiscount());
    }
}