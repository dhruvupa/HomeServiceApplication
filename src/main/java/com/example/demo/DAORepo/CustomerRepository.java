package com.example.demo.DAORepo;

import java.sql.ResultSet;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Booking.Booking;
import com.example.demo.Model.Booking.BookingRowMapper;
import com.example.demo.Model.Booking.ServiceProviderBookingDTO;
import com.example.demo.Model.Customer.*;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.SearchServices.SearchServiceRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

@Repository
public class CustomerRepository {

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

    //Query to find if customer exists while creating new account
	public List<Map<String, Object>> findcustomer(String name) {
    	String query = "SELECT name FROM customer WHERE LOWER(name) = LOWER(?)";
    	return queryTemplate.queryForList(query,new Object[]{name});
    }
	
	//Query to new customer to the database
	public int addCustomer(Customer customer) {
	    // Encrypt the password
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String hashedPassword = passwordEncoder.encode(customer.getPassword());

	    // Insert query
	    String query = "INSERT INTO CUSTOMER (name, email, password, phone_number, city) VALUES (?, ?, ?, ?, ?)";

	    // Execute query and retrieve the generated ID
	    queryTemplate.update(query, 
	        customer.getName(), 
	        customer.getEmail(), 
	        hashedPassword, 
	        customer.getPhoneno(), 
	        customer.getCity());

	    // Assuming the ID is auto-generated, we can use a SELECT query to retrieve it
	    String idQuery = "SELECT LAST_INSERT_ID()";
	    return queryTemplate.queryForObject(idQuery, Integer.class);  // Returns the last generated ID
	}

	
	//Query to check for login with and password
	public List<Map<String, Object>> loginCustomer(String name, String password){
		String query = "SELECT name, password FROM customer WHERE LOWER(name) = LOWER(?)";
		return queryTemplate.queryForList(query,new Object[]{name});
	}
	
	//Query to display the name on the html page
	public Customer getCustomerByNamequery(String name) {
		String query = "SELECT * FROM customer WHERE name = ?";
		List<Customer> customers= queryTemplate.query(query, new CustomerRowMapper(),name);
		return customers.isEmpty() ? null : customers.get(0);
	}

	public List<Customer> findCustomerByCity(String city) {
		String query = "SELECT * FROM customer WHERE city = ?";
		List<Customer> customers= queryTemplate.query(query, new CustomerRowMapper(),city);
		return customers;
	}

	@SuppressWarnings("deprecation")
	public List<ServiceProviderBookingDTO> findAllCustomerCurrentBooking(String custName) {
		String queryString =  """
                SELECT 
                b.booking_id AS booking_id,
                b.status AS booking_status,
                b.booking_date,
                c.name AS customer_name,
                ss.skill
            FROM 
                booking b
            JOIN 
                customer c ON b.customer_id = c.customer_id
            JOIN 
                searchservice ss ON b.service_id = ss.service_id
            WHERE
                c.name = ? AND b.status IN ('ACCEPTED');""";

        return queryTemplate.query(queryString, new Object[]{custName}, new BookingRowMapper());
	}

	public int cancelCustomerCurrentBooking(String bookingId) {
		  String query = "UPDATE booking SET status = 'REJECTED' WHERE booking_id = ?";
		  queryTemplate.update(query, bookingId);
		  return 1;
	}
	
	@SuppressWarnings("deprecation")
	public List<Booking> findAllCustomerPastBooking(String cusName)
	{
		String queryString =  """
                SELECT 
                b.booking_id AS booking_id,
                b.status AS booking_status,
                b.booking_date,
                b.service_id,
                c.name AS customer_name,
                ss.skill
            FROM 
                booking b
            JOIN 
                customer c ON b.customer_id = c.customer_id
            JOIN 
                searchservice ss ON b.service_id = ss.service_id
            WHERE
                c.name = ? AND b.status IN ('COMPLETED');""";

        return queryTemplate.query(queryString, new Object[]{cusName}, new RatingRowMapping());
	}
	
	public int updateRating(int service_id,String rating) {
		
		String queryString="UPDATE searchservice SET rating = ? WHERE service_id=?;";
		return queryTemplate.update(queryString,new Object[]{rating,service_id});
	}
}