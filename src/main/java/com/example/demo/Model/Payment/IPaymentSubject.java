package com.example.demo.Model.Payment;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;

public interface IPaymentSubject {
    void addObserver(Customer observer, ServiceProvider observer2);
    void notifyObservers();

}
