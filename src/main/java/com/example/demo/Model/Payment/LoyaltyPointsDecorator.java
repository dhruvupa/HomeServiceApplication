package com.example.demo.Model.Payment;

public class LoyaltyPointsDecorator extends PaymentDecorator {

    public LoyaltyPointsDecorator(BasePayment payment) {
        super(payment);
    }

    @Override
    public float processPayment() {
        float loyaltyDiscountedAmount = decoratedPayment.getAmount() * 0.90f; // Apply 10% discount
        decoratedPayment.setAmount(loyaltyDiscountedAmount);
        System.out.println("10% Loyalty Discount Applied.");
        return decoratedPayment.processPayment();
    }
}