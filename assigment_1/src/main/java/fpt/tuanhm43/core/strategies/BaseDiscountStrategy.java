package fpt.tuanhm43.core.strategies;

import fpt.tuanhm43.core.entities.Product;

public interface BaseDiscountStrategy {
    double applyDiscount(Product product);
}