package fpt.tuanhm43.core.strategies;

import fpt.tuanhm43.core.entities.Product;

public class NoDiscount implements BaseDiscountStrategy {
    public double applyDiscount(Product product) {
        return product.getPrice();
    }
}
