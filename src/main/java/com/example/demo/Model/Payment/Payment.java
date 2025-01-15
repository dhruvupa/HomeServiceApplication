package com.example.demo.Model.Payment;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class Payment extends BasePayment  {

    public Payment(float amount, String status) {
        super(amount, status);
    }

    @Override
    public float processPayment() {
        this.setStatus("COMPLETED");
        notifyObservers();
        return (int) getAmount();
    }

}