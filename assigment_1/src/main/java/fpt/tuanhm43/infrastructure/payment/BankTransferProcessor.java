package fpt.tuanhm43.infrastructure.payment;

import fpt.tuanhm43.core.interfaces.IPaymentProcessor;

public class BankTransferProcessor implements IPaymentProcessor {
    @Override
    public double calculateFee(double amount) {
        return amount;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Waiting for bank transfer confirmation...");
        return true;
    }

    @Override
    public String getMethodName() {
        return "BANK_TRANSFER";
    }
}
