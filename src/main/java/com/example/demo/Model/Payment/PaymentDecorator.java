package com.example.demo.Model.Payment;

public abstract class PaymentDecorator extends BasePayment {
    protected BasePayment decoratedPayment;

    public PaymentDecorator(BasePayment payment) {
        super(payment.getAmount(), payment.getStatus());
        this.decoratedPayment = payment;
    }

    @Override
    public float processPayment() {
        notifyObservers();
        return decoratedPayment.processPayment();
    }
}