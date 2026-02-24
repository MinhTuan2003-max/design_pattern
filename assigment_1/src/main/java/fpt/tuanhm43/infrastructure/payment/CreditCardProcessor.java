package fpt.tuanhm43.infrastructure.payment;

import fpt.tuanhm43.core.interfaces.IPaymentProcessor;

public class CreditCardProcessor implements IPaymentProcessor {
    @Override
    public double calculateFee(double amount) {
        return amount * 1.03;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing credit card payment...");
        return Math.random() > 0.1;
    }

    @Override
    public String getMethodName() {
        return "CREDIT_CARD";
    }
}
