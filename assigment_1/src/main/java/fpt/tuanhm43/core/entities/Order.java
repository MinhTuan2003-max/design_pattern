package fpt.tuanhm43.core.entities;

import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;
    private String shippingAddress;

    public Order(String id, String customerId, List<OrderItem> items, double totalAmount, String shippingAddress) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.status = "CONFIRMED";
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}