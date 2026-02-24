package fpt.tuanhm43.core.interfaces;

import fpt.tuanhm43.core.entities.Product;

public interface IProductRepository {
    Product findById(String id);

    void update(Product product);
}
