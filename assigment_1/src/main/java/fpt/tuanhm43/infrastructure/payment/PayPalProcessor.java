package fpt.tuanhm43.infrastructure.payment;

import fpt.tuanhm43.core.interfaces.IPaymentProcessor;

public class PayPalProcessor implements IPaymentProcessor {
    @Override
    public double calculateFee(double amount) {
        return amount * 1.025;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Redirecting to PayPal...");
        return Math.random() > 0.05;
    }

    @Override
    public String getMethodName() {
        return "PAYPAL";
    }
}
