package com.example.demo.Model.Admin;

import org.springframework.stereotype.Component;

import com.example.demo.Model.User.User;

@Component
public class Admin extends User{
	
	@Override
    public String getUserType() {
        return "Admin";
    }
}
