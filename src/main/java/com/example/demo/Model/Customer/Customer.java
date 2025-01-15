package com.example.demo.Model.Customer;
import org.springframework.stereotype.Component;

import com.example.demo.Model.User.User;

@Component
public class Customer extends User implements ICustomerObserver{

	@Override
    public String getUserType() {
        return "Customer";
    }

	@Override
	public void update(String message, String email) {
		// TODO Auto-generated method stub
		System.out.println("Sending notification to " + email + ": " + message);
	}

}
