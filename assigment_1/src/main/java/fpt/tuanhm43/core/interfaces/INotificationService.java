package fpt.tuanhm43.core.interfaces;

import fpt.tuanhm43.core.entities.Order;

public interface INotificationService {
    void notifyOrderCreated(Order order, String email, String paymentMethod);

    void notifyOrderCancelled(Order order);

    void notifyOrderShipped(Order order, String trackingNumber);
}