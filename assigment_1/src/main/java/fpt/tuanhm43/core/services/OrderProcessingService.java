package fpt.tuanhm43.core.services;

import fpt.tuanhm43.core.entities.Order;
import fpt.tuanhm43.core.entities.OrderItem;
import fpt.tuanhm43.core.entities.Product;
import fpt.tuanhm43.core.interfaces.INotificationService;
import fpt.tuanhm43.core.interfaces.IOrderRepository;
import fpt.tuanhm43.core.interfaces.IPaymentProcessor;
import fpt.tuanhm43.core.interfaces.IProductRepository;
import fpt.tuanhm43.core.strategies.*;

import java.util.*;

public class OrderProcessingService {
    private final IProductRepository productRepo;
    private final IOrderRepository orderRepo;
    private final INotificationService notificationService;
    private final DiscountStrategyFactory discountFactory;

    public OrderProcessingService(
            IProductRepository productRepo,
            IOrderRepository orderRepo,
            INotificationService notificationService,
            DiscountStrategyFactory discountFactory) {

        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.notificationService = notificationService;
        this.discountFactory = discountFactory;
    }

    public String createOrder(String customerId, String customerEmail, List<String> productIds,
                              IPaymentProcessor paymentProcessor, String shippingAddress) {
        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (String productId : productIds) {
            Product product = productRepo.findById(productId);
            if (product == null || product.getStock() <= 0) return null;

            BaseDiscountStrategy discountStrategy = discountFactory.getStrategy(product.getCategory());
            double finalPrice = discountStrategy.applyDiscount(product);

            total += finalPrice;
            orderItems.add(new OrderItem(productId, product.getName(), finalPrice));

            product.decreaseStock();
            productsToUpdate.add(product);
        }

        total = paymentProcessor.calculateFee(total);
        if (!paymentProcessor.processPayment()) {
            productsToUpdate.forEach(Product::increaseStock);
            return null;
        }

        String orderId = "ORD-" + System.currentTimeMillis();
        Order order = new Order(orderId, customerId, orderItems, total, shippingAddress);
        orderRepo.save(order);

        productsToUpdate.forEach(productRepo::update);
        notificationService.notifyOrderCreated(order, customerEmail, paymentProcessor.getMethodName());
        return orderId;
    }

    public void shipOrder(String orderId, String trackingNumber) {
        Order order = orderRepo.findById(orderId);
        if (order != null && "CONFIRMED".equals(order.getStatus())) {
            order.setStatus("SHIPPED");
            orderRepo.save(order);
            notificationService.notifyOrderShipped(order, trackingNumber);
        }
    }

    public void printAllOrders() {
        orderRepo.findAll().forEach(order ->
                System.out.println("Order: " + order.getId() + " - Status: " + order.getStatus() + " - Total: $" + order.getTotalAmount())
        );
    }
}
