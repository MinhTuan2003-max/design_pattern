package fpt.tuanhm43.core.entities;

public class OrderItem {
    private String productId;
    private String productName;
    private double finalPrice;

    public OrderItem(String productId, String productName, double finalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.finalPrice = finalPrice;
    }

    public String getProductId() {
        return productId;
    }
}