package fpt.tuanhm43.infrastructure.data;

import fpt.tuanhm43.core.entities.Order;
import fpt.tuanhm43.core.interfaces.IOrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOrderRepository implements IOrderRepository {
    private Map<String, Order> db = new HashMap<>();

    public void save(Order order) {
        db.put(order.getId(), order);
    }

    public Order findById(String id) {
        return db.get(id);
    }

    public List<Order> findAll() {
        return new ArrayList<>(db.values());
    }
}