package com.example.demo.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.Model.User.ConcreteUserFactory;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Model.User.User;
import com.example.demo.Model.Wallet.Wallet;
import com.example.demo.Service.Customer.CustomerService;
import com.example.demo.Service.SearchService.SearchServiceService;
import com.example.demo.Service.WalletService.WalletService;
import com.example.demo.Model.Booking.Booking;
import com.example.demo.Model.Booking.ServiceProviderBookingDTO;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.SearchServices.SearchService;

@RestController
@RequestMapping("/Customer")
public class CustomerController {
	
	public static String globalCustomername;
	public static final String customerString="customer";
	public String getGlobalCustomername() {
		return globalCustomername;
	}

	public void setGlobalCustomername(String globalCustomername) {
		this.globalCustomername = globalCustomername;
	}
	
    private final CustomerService customerService;
    private IUserFactory userFactory;
    
    private SearchServiceService searchServiceService;
    
    private WalletService walletService;
    
    public CustomerController(CustomerService customerService,IUserFactory userFactory,SearchServiceService searchServiceService,WalletService walletService) {
        this.customerService = customerService;
        this.userFactory=userFactory;
        this.searchServiceService =searchServiceService;
        this.walletService= walletService;
    }
    
    /*---------------Customer Registration---------------------------*/
    @GetMapping("/customerRegistrationForm")
    public String checkUser(Model model){
    	userFactory=new ConcreteUserFactory();
    	
    	model.addAttribute(customerString,userFactory.createUser("Customer"));
    	return "CustomerRegistration";
	}
    
   
    @PostMapping("/customerRegistrationSuccess")
    @ResponseBody
    public String createCustomer(Customer customer) {
        try {
            Customer newCustomer = customerService.addCustomer(customer); // Get the customer with ID
            // Create wallet for the new customer with an initial balance of 100
            walletService.createWallet(newCustomer.getId(), "CUSTOMER", 100.0f);
            System.out.println("HI NEW CUSTOMER: " + newCustomer); // Log the new customer
            
            return "User Created Successfully";
        } 
        catch (IllegalArgumentException e) {
            return e.getMessage(); // User already exists
        } 
        catch (Exception e) {
            e.printStackTrace();
            
            return "An error occurred. Please try again.";
        }
    }
    /*---------------Customer Registration---------------------------*/
    
    /*---------------Customer Login---------------------------*/
    @GetMapping("/customerLogin")
    public String customerLogin(Model model) {
    	return "CustomerLogin";
    }
    
    @PostMapping("/customerLoginSuccess")
    public String authenticateCustomer(@RequestParam("username") String name, @RequestParam String password,Model model) {
    	int response=customerService.checkCustomerLogin(name,password);
    	if(response==1) {
    		this.setGlobalCustomername(name);
            return "CustomerWelcomeScreen";
    	}
    	else { 
    		User customer = userFactory.createUser("Customer");
        	model.addAttribute(customerString,customer);
        	return "CustomerRegistration";
    	}
    }
    /*---------------Customer Login---------------------------*/
    
    /*---------------Search Service---------------------------*/
    @GetMapping("/allservices")
    public String getAllservices(Model model) {
    	List<SearchService> services = searchServiceService.getAllServices();
		Customer customer=customerService.getCustomerByName(this.getGlobalCustomername());
		this.setGlobalCustomername(customer.getName());
		
		// Get Customer's balance
		float walletBalance = walletService.getWalletBalanceByUserId(customer.getId(), "CUSTOMER");
		
		model.addAttribute(customerString,customer);
        model.addAttribute("services", services);
        model.addAttribute("walletBalance", walletBalance); // Add balance to model
        
        return "all-services";
    }
    
    @GetMapping("/filterservice")
    public String listFilteredServices(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status,
            Model model) {

        // Fetch the filtered services for the customer
        List<SearchService> services = customerService.searchService(skill, city, status);
        Customer customer=customerService.getCustomerByName(this.getGlobalCustomername());
		model.addAttribute(customerString,customer);
        model.addAttribute("services", services);
        return "filter-services"; // Refers to the Thymeleaf view
    }
    /*---------------Search Service---------------------------*/
    
    /*---------------Rate Service---------------------------*/
    @GetMapping("/rateService")
    public String rateService(Model model)
    {
    	List<Booking> booking = customerService.getAllPastBooking(this.getGlobalCustomername());
		model.addAttribute("Booking",booking);
        return "CustomerPastServices";
    }
    
    @PostMapping("/postRating")
    public String postService(@RequestParam int serviceid,@RequestParam String rating) {
    	customerService.postRating(serviceid,rating);
    	return "Rating posted";
		
    }
    /*---------------Rate Service---------------------------*/
    
    @GetMapping("/book")
    public String bookservice(@RequestParam String serviceId, @RequestParam String customerCity, @RequestParam String serviceCity,
    		@RequestParam String serviceStatus,@RequestParam String servicePrice, @RequestParam String serviceSkill, 
    		@RequestParam String serviceRating, @RequestParam String serviceProviderName, @RequestParam String serviceCategory,Model model) {
    	String response=customerService.checkServiceParameters(customerCity,serviceCity,serviceStatus);
    	if(response=="success")
    	{
    		model.addAttribute("serviceId",serviceId);
    		model.addAttribute("customerCity", customerCity); 
    		model.addAttribute("serviceCity", serviceCity); 
    		model.addAttribute("serviceStatus", serviceStatus);
    		model.addAttribute("servicePrice", servicePrice); 
    		model.addAttribute("serviceSkill", serviceSkill); 
    		model.addAttribute("serviceRating", serviceRating);
    		model.addAttribute("serviceProviderName", serviceProviderName);
    		model.addAttribute("serviceCategory", serviceCategory);
    		return "bookingscreen";
    	}
    	else
    	{
    		return response;
    	}
    }
    
    
    
    @GetMapping("/CustomerCurrentBookedServices")
    public String getCustomerCurrentBooking(Model model) {
    	List<ServiceProviderBookingDTO> booking = customerService.getAllCurrentBooking(this.getGlobalCustomername());
		model.addAttribute("ServiceProviderBookingDTO",booking);
        return "CustomerCurrentBookedServices";
    }
    
    @PostMapping("/CancelBooking/{bookingId}/{bookingStatus}")
    public RedirectView deleteService(@PathVariable("bookingId") String bookingId,@PathVariable("bookingStatus") String bookingStatus,RedirectAttributes redirectAttributes) {
    	int res = customerService.cancelSelectedBooking(bookingId,bookingStatus);
    	if(res!=0) {
    		redirectAttributes.addFlashAttribute("message", "Booking Status Rejected Successfully!!");
    		return new RedirectView("/Customer/CustomerCurrentBookedServices");
    	}
    	redirectAttributes.addFlashAttribute("message", "Booking status cannot be rejected");
    	return new RedirectView("/Customer/CustomerCurrentBookedServices");
    }
    
}
