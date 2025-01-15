package com.example.demo.Service.Admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.DAORepo.AdminRepository;
import com.example.demo.Model.Admin.Admin;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;

@Service
public class AdminService {
	private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository  = adminRepository;
    }
    
    //Logic to check if User already exists or not.
    public int addAdmin(Admin admin) {
    	List<Map<String, Object>> response=adminRepository.findAdmin(admin.getName());
    	if(!response.isEmpty()) {
    		return 0;    		
    	}
    	else
    	{
    		return adminRepository.addadmin(admin);
    	}

    }
    
    public List<Customer> getAllCustomers() {
    	return adminRepository.getAllCustomers();
    }
    public List<ServiceProvider> getAllServiceProviders() {
        return adminRepository.getAllServiceProviders();
    }

    // Logic to delete user
    public int deleteUser(int id, String type) {
    	if(type == "customer") {
    		if (!adminRepository.customerExistsById(id)) {
    			throw new RuntimeException("User not found");
    		}
    		return adminRepository.deleteCustomerById(id);    		
    	}
    	else {
    		if (!adminRepository.serviceProviderExistsById(id)) {
    			throw new RuntimeException("User not found");
    		}
    		return adminRepository.deleteServiceProviderById(id);
    	}
    }
}
