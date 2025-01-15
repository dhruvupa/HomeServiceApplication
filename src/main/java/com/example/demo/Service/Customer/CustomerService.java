package com.example.demo.Service.Customer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DAORepo.CustomerRepository;
import com.example.demo.Model.Booking.Booking;
import com.example.demo.Model.Booking.ServiceProviderBookingDTO;
//import com.example.demo.Model.Customer.*;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Service.SearchService.SearchServiceService;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    
    @Autowired
    private SearchServiceService searchServiceService;
    
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    //Logic to check if User already exists or not.
    public Customer addCustomer(Customer customer) {
        List<Map<String, Object>> response = customerRepository.findcustomer(customer.getName());
        
        if (!response.isEmpty()) {
            throw new IllegalArgumentException("User Already Exists");
        } 
        else {
            int generatedId = customerRepository.addCustomer(customer);
            customer.setId(generatedId); // Set the generated ID in the Customer object
        
            return customer;
        }
    }
    
    //Login logic check
    public int checkCustomerLogin(String name, String password) {
    	List<Map<String, Object>> response= customerRepository.loginCustomer(name,password);
    	if(!response.isEmpty())
    	{
    		Map<String,Object> firstResult=response.get(0);
        	String getname=(String)firstResult.get("name");
        	System.out.println(getname);
        	String storedHashedPassword=(String)firstResult.get("password");
        	System.out.println(storedHashedPassword);
        	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean passwordMatches = passwordEncoder.matches(password, storedHashedPassword);
	    	if(!getname.equals(name) || passwordMatches!=true) {
	    		return 0;    		
	    	}
	    	else
	    	{
	    		return 1;
	    	}
    	}
    	else
    	{
    		return 0;
    	}
    }
    
    
    // Directly call the filtering method from SearchServiceService
    public List<SearchService> searchService(String skill, String city, String status) {
        
        return searchServiceService.filterServices(skill, city, status);
    }
    
    public Customer getCustomerByName(String name) {
    	return customerRepository.getCustomerByNamequery(name);
    }
    
    public String checkServiceParameters(String customerCity,String serviceCity,String serviceStatus) {
    	if (!customerCity.toLowerCase().equals(serviceCity.toLowerCase()) || serviceStatus.toLowerCase().equals("busy")) {
    	    return "The selected service is not in your city or the service provider is busy";
    	} else {
    	    return "success";
    	}

    }

	public List<Customer> getAllCustomersByCity(String city) {
		// TODO Auto-generated method stub
		return customerRepository.findCustomerByCity(city);
	}

	public List<ServiceProviderBookingDTO> getAllCurrentBooking(String custName) {
		return customerRepository.findAllCustomerCurrentBooking(custName);
	}

	public int cancelSelectedBooking(String bookingId,String bookingStatus) {
		if(bookingStatus.equalsIgnoreCase("ACCEPTED")) {
			return customerRepository.cancelCustomerCurrentBooking(bookingId);
		}
		return 0;
	}
	
	public List<Booking> getAllPastBooking(String cusName) {
		return customerRepository.findAllCustomerPastBooking(cusName);
	}
	
	public int postRating(int service_id, String rating) {
		return customerRepository.updateRating(service_id,rating);
	}
}