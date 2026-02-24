package fpt.tuanhm43.core.interfaces;

import fpt.tuanhm43.core.entities.Order;

import java.util.List;

public interface IOrderRepository {
    void save(Order order);

    Order findById(String id);

    List<Order> findAll();
}