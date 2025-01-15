package com.example.demo.Model.Payment;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePayment implements IPaymentSubject {
    private String bookingId;
    private float amount;
    private String status;
    private String timestamp;

    private final List<Customer> customerObservers = new ArrayList<>();
    private final List<ServiceProvider> serviceProviderObservers = new ArrayList<>();

    public BasePayment(float amount, String status) {
        this.amount = amount;
        this.status = status;
    }

    // Abstract method for processing payment
    public abstract float processPayment();

    // Common methods for all payments
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public void addObserver(Customer observer, ServiceProvider observer2) {
        customerObservers.add(observer);
        serviceProviderObservers.add(observer2);
    }

    @Override
    public void notifyObservers() {
        for(Customer custObs : customerObservers){
            custObs.update(custObs.getName()+" Customer Notified",custObs.getEmail());
        }
        for(ServiceProvider serObs : serviceProviderObservers){
            serObs.update(serObs.getName()+"  Service Provider Notified ",serObs.getEmail());
        }
    }

}
