package com.example.demo.DAORepo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Admin.Admin;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.Customer.CustomerRowMapper;
import com.example.demo.Model.Admin.ServiceProviderRowMapperForAdmin;
import com.example.demo.Model.ServiceProvider.ServiceProvider;

@Repository
public class AdminRepository {
private JdbcTemplate queryTemplate;
	
	//getter for template
    public JdbcTemplate getQueryTemplate() {
		return queryTemplate;
	}

    //setter for template
    @Autowired //This is managed by springboot now
	public void setQueryTemplate(JdbcTemplate queryTemplate) {
		this.queryTemplate = queryTemplate;
	}

	public List<Map<String, Object>> findAdmin(String name) {
    	String query = "SELECT name FROM admin WHERE LOWER(name) = LOWER(?)";
    	return queryTemplate.queryForList(query,new Object[]{name});
    }
	
	public int addadmin(Admin admin) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(admin.getPassword());
		String query="INSERT INTO ADMIN (name,email,password) VALUES (?,?,?)";
		return queryTemplate.update(query, admin.getName(),admin.getEmail(),hashedPassword);
	}
	
	//Query to get all the customer's accounts
	public List<Customer> getAllCustomers() {
		String query = "SELECT * FROM customer";
		List<Customer> customers = queryTemplate.query(query, new CustomerRowMapper());
		return customers.isEmpty() ? null : customers;
	}
	
	//Query to delete the customer
	public int deleteCustomerById(int customer_id) {
	    String query = "DELETE FROM customer WHERE customer_id = ?";
	    return queryTemplate.update(query, customer_id);
	}

	//Query to check if user exist with user id
	public boolean customerExistsById(int id) {
	    String query = "SELECT COUNT(*) FROM customer WHERE customer_id = ?";
	    Integer count = queryTemplate.queryForObject(query, Integer.class, id);
	    return count != null && count > 0;
	}
	
	//Query to get all the service provider's accounts
	public List<ServiceProvider> getAllServiceProviders() {
		String query = "SELECT * FROM serviceprovider";
		List<ServiceProvider> serviceProvider = queryTemplate.query(query, new ServiceProviderRowMapperForAdmin());
		return serviceProvider.isEmpty() ? null : serviceProvider;
	}
	
	//Query to delete the service provider
	public int deleteServiceProviderById(int provider_id) {
	    String query = "DELETE FROM serviceprovider WHERE provider_id = ?";
	    return queryTemplate.update(query, provider_id);
	}

	//Query to check if user exist with user id
	public boolean serviceProviderExistsById(int id) {
	    String query = "SELECT COUNT(*) FROM serviceprovider WHERE provider_id = ?";
	    Integer count = queryTemplate.queryForObject(query, Integer.class, id);
	    return count != null && count > 0;
	}
}
