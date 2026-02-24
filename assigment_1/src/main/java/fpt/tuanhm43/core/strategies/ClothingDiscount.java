package fpt.tuanhm43.core.strategies;

import fpt.tuanhm43.core.entities.Product;

public class ClothingDiscount implements BaseDiscountStrategy {
    public double applyDiscount(Product product) {
        return product.getPrice() * 0.90;
    }
}
