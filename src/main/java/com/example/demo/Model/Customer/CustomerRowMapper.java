package com.example.demo.Model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> { 
	@Override
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException { 
		Customer customer = new Customer();  
		customer.setId(rs.getInt("customer_id"));
		customer.setName(rs.getString("name")); 
		customer.setEmail(rs.getString("email")); 
		customer.setPassword(rs.getString("password")); 
		customer.setPhoneno(rs.getInt("phone_number")); 
		customer.setCity(rs.getString("city"));
		customer.setId(rs.getInt("customer_id"));
		return customer;
	} 
}
