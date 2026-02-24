package fpt.tuanhm43.core.strategies;

import fpt.tuanhm43.core.entities.Product;

public class ElectronicsDiscount implements BaseDiscountStrategy {
    public double applyDiscount(Product product) {
        return product.getPrice() > 500 ? product.getPrice() * 0.95 : product.getPrice();
    }
}