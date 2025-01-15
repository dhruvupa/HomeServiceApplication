package com.example.demo.Model.ServiceProvider;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.Customer.ICustomerObserver;

public interface IServiceProviderSubject {
    void addObserver(Customer observer);
    void notifyObservers(String message);
}
