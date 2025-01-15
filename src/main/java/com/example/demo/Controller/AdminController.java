package com.example.demo.Controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Model.Admin.Admin;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;
import com.example.demo.Model.User.ConcreteUserFactory;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Model.User.User;
import com.example.demo.Service.Admin.*;
import com.example.demo.Service.ServiceProvider.ServiceProviderService;

@Controller
@RequestMapping("/Admin")
public class AdminController {
	private final AdminService adminservice;
    private IUserFactory userFactory;
    private final ServiceProviderService serviceProviderService;
    
    public AdminController(AdminService adminservice, IUserFactory userFactory, ServiceProviderService serviceProviderService) {
        this.adminservice = adminservice;
        this.userFactory = userFactory;
        this.serviceProviderService = serviceProviderService;
    }
    

    @GetMapping("/adminRegistrationForm")
    public String checkUser(Model model){
    	userFactory=new ConcreteUserFactory();
    	model.addAttribute("admin",userFactory.createUser("Admin"));
    	return "AdminRegistration";
	}
    
   
    @PostMapping("/adminRegistrationSuccess")
    @ResponseBody
    public String createAdmin(Admin admin) throws SQLException {
    	int response= adminservice.addAdmin(admin);
    	if(response==0) {
    		return "Admin Already Exists";
    	}
    	else
    	{
    		return "Admin Created Successfully";
    	}
    }
    
    @GetMapping("/manage-accounts")
    public String manageAccounts(Model model) {
//        List<Customer> users = adminservice.getAllUsers();
//        model.addAttribute("users", users); 
    	List<Customer> customers = adminservice.getAllCustomers();
        List<ServiceProvider> serviceProviders = adminservice.getAllServiceProviders();

        model.addAttribute("customers", customers);
        model.addAttribute("serviceProviders", serviceProviders);
        return "manageAccountScreen"; 
    }
    
    @GetMapping("/manage-accounts/customer/{id}")
    public String deleteCustomer(@PathVariable int id, Model model) {
        int rowsAffected = adminservice.deleteUser(id, "customer");
        if (rowsAffected > 0) {
            model.addAttribute("status", "Customer deleted successfully");
        } else {
            model.addAttribute("status", "Customer not found");
        }
        // Reload the updated lists
        model.addAttribute("customers", adminservice.getAllCustomers());
        model.addAttribute("serviceProviders", adminservice.getAllServiceProviders());
        return "manageAccountScreen";
    }

    
    @GetMapping("/manage-accounts/service-provider/{id}")
    public String deleteServiceProvider(@PathVariable int id, Model model) {
        int rowsAffected = adminservice.deleteUser(id, "serviceProvider");
        if (rowsAffected > 0) {
            model.addAttribute("status", "Service Provider deleted successfully");
        } else {
            model.addAttribute("status", "Service Provider not found");
        }
        // Reload the updated lists
        model.addAttribute("customers", adminservice.getAllCustomers());
        model.addAttribute("serviceProviders", adminservice.getAllServiceProviders());
        return "manageAccountScreen";
    }

}
