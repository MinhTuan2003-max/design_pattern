package fpt.tuanhm43.presentation;

import fpt.tuanhm43.core.services.OrderProcessingService;
import fpt.tuanhm43.core.strategies.*;
import fpt.tuanhm43.infrastructure.data.InMemoryOrderRepository;
import fpt.tuanhm43.infrastructure.data.InMemoryProductRepository;
import fpt.tuanhm43.infrastructure.payment.CreditCardProcessor;
import fpt.tuanhm43.infrastructure.services.ConsoleNotificationService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        InMemoryProductRepository productRepo = new InMemoryProductRepository();
        InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();
        ConsoleNotificationService notificationService = new ConsoleNotificationService();

        Map<String, BaseDiscountStrategy> strategies = new HashMap<>();
        strategies.put("ELECTRONICS", new ElectronicsDiscount());
        strategies.put("CLOTHING", new ClothingDiscount());
        strategies.put("DEFAULT", new NoDiscount());

        DiscountStrategyFactory discountFactory = new DiscountStrategyFactory(strategies);

        OrderProcessingService coreService = new OrderProcessingService(
                productRepo,
                orderRepo,
                notificationService,
                discountFactory
        );

        String orderId = coreService.createOrder(
                "CUST-001",
                "john@example.com",
                Arrays.asList("P003", "P004"),
                new CreditCardProcessor(),
                "123 Main St, City"
        );

        if (orderId != null) {
            coreService.shipOrder(orderId, "TRACK-12345");
        }

        coreService.printAllOrders();
    }
}