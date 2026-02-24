package fpt.tuanhm43.infrastructure.data;

import fpt.tuanhm43.core.entities.Product;
import fpt.tuanhm43.core.interfaces.IProductRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProductRepository implements IProductRepository {
    private Map<String, Product> db = new HashMap<>();

    public InMemoryProductRepository() {
        db.put("P001", new Product("P001", "Laptop", 999.99, 50, "ELECTRONICS"));
        db.put("P002", new Product("P002", "T-Shirt", 29.99, 200, "CLOTHING"));
        db.put("P003", new Product("P003", "Coffee Beans", 15.99, 100, "FOOD"));
        db.put("P004", new Product("P004", "Headphones", 149.99, 75, "ELECTRONICS"));
    }

    public Product findById(String id) {
        return db.get(id);
    }

    public void update(Product product) {
        db.put(product.getId(), product);
    }
}
