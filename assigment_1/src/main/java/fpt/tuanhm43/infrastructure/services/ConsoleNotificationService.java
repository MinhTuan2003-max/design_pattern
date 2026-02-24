package fpt.tuanhm43.infrastructure.services;

import fpt.tuanhm43.core.entities.Order;
import fpt.tuanhm43.core.interfaces.INotificationService;

public class ConsoleNotificationService implements INotificationService {

    @Override
    public void notifyOrderCreated(Order order, String email, String paymentMethod) {
        System.out.println("Sending email to " + email + "...");
        System.out.println("Subject: Order Confirmed - " + order.getId());
        System.out.println("Body: Your order total is $" + String.format("%.2f", order.getTotalAmount()));

        System.out.println("[LOG] Order created: " + order.getId() + " for customer " + order.getCustomerId());

        System.out.println("[ANALYTICS] New order: $" + order.getTotalAmount() + " via " + paymentMethod);
    }

    @Override
    public void notifyOrderCancelled(Order order) {
        System.out.println("[EMAIL] Your order " + order.getId() + " has been cancelled.");
        System.out.println("[LOG] Order cancelled: " + order.getId());
    }

    @Override
    public void notifyOrderShipped(Order order, String trackingNumber) {
        System.out.println("[EMAIL] Your order " + order.getId() + " has been shipped!");
        System.out.println("Tracking: " + trackingNumber);
        System.out.println("[SMS] Order shipped: " + trackingNumber);
        System.out.println("[LOG] Order shipped: " + order.getId());
    }
}
