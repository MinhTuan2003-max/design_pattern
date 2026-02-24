package fpt.tuanhm43.core.interfaces;

public interface IPaymentProcessor {
    double calculateFee(double amount);

    boolean processPayment();

    String getMethodName();
}
