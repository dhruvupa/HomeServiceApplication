package com.example.demo.Model.Payment;

public class DiscountDecorator extends PaymentDecorator {

    public DiscountDecorator(BasePayment payment) {
        super(payment);
    }

    @Override
    public float processPayment() {
        float discountedAmount = decoratedPayment.getAmount() * 0.95f; // Apply 5% discount
        decoratedPayment.setAmount(discountedAmount);
        System.out.println("5% Discount Applied.");
        return decoratedPayment.processPayment();
    }
}