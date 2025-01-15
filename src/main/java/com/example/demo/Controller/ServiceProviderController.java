package com.example.demo.Controller;

import com.example.demo.Model.Booking.ServiceProviderBookingDTO;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Service.ServiceProvider.GlobalContext;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.ServiceProvider.ServiceProvider;
import com.example.demo.Model.ServiceProvider.ServiceProviderStateManager;
import com.example.demo.Model.User.ConcreteUserFactory;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Model.User.User;
import com.example.demo.Service.Customer.CustomerService;
import com.example.demo.Service.ServiceProvider.ServiceProviderService;
import com.example.demo.Service.WalletService.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;

@Controller
@RequestMapping("/ServiceProvider")
public class ServiceProviderController {

	public static final String SEARCH_SERVICE = "SearchService";
	public static final String SERVICE_PROVIDER = "ServiceProvider";
	
    private final ServiceProviderService serviceproviderservice;
    
    @Autowired
    private final ServiceProviderStateManager serviceProviderStateManager;
    
    private final CustomerService customerService;
    
    private IUserFactory userFactory;
    
	private  final GlobalContext globalcontext; 
	
	@Autowired
	private WalletService walletService;  // Inject the WalletService to fetch wallet balance

    public ServiceProviderController(ServiceProviderService serviceproviderservice, IUserFactory userFactory,GlobalContext globalcontext, ServiceProviderStateManager serviceProviderStateManager, CustomerService customerService,WalletService walletService) {
        this.serviceproviderservice = serviceproviderservice;
		this.customerService = customerService;
        this.userFactory = userFactory;
        this.globalcontext=globalcontext;
        this.serviceProviderStateManager=serviceProviderStateManager;
        this.walletService= walletService;
    }

    // Endpoint to register a new service provider
    //Achyutam
    @GetMapping("/ServiceProviderRegistrationForm")
    public String checkServiceProvider(Model model)
    {
    	userFactory=new ConcreteUserFactory();
        model.addAttribute(SERVICE_PROVIDER, userFactory.createUser("ServiceProvider"));
        return "ServiceProviderRegistration";
    }

    // Register a new ServiceProvider
    //Achyutam
    @PostMapping("/ServiceProviderRegistrationSuccess")
    @ResponseBody
    public RedirectView createServiceProvider(ServiceProvider serviceProvider) {
        int response = serviceproviderservice.VerifyifServiceProviderExist(serviceProvider);
        
        if (response == 0) {
            return new RedirectView("/ServiceProvider/ServiceProviderLoginForm?error=UserAlreadyExists");
        } 
        else {
        	// Redirect to login form after successful registration
            return new RedirectView("/ServiceProvider/ServiceProviderLoginForm");
        }
    }



    //Achyutam
    @GetMapping("/ServiceProviderLoginForm")
    public String loginForm(Model model)
    {
        ServiceProvider serviceProvider = new ServiceProvider();
        model.addAttribute(SERVICE_PROVIDER, serviceProvider);
        return "ServiceProvider_login";
    }

    //Achyutam
    @PostMapping("/loginsucess")
    @ResponseBody
    public RedirectView loginServiceProvider(@RequestParam String email, @RequestParam String password,RedirectAttributes redirectAttributes) {
        boolean isAuthenticated = serviceproviderservice.validateLogin(email, password);
        if (isAuthenticated) {
        	return new RedirectView("ServiceProviderWelcomeScreen");
            //return "Login Successful";
//            model.addAttribute("email", email);
//            return "ServiceProviderDashboard";
        } else {
            //return "Invalid email or password";
        	 redirectAttributes.addFlashAttribute("error", "Invalid email or password");
             return new RedirectView("ServiceProviderLoginForm");
        }
    }

    //Dhruv
    @GetMapping("/ServiceProviderWelcomeScreen")
    public String serviceProviderForm(Model model){
    	// Get the current logged-in service provider's ID
        String serviceProviderId = globalcontext.getServiceProviderId();
        
        // Fetch the wallet balance using the WalletService
        float walletBalance = walletService.getWalletBalanceByUserId(Integer.parseInt(serviceProviderId), "SERVICE_PROVIDER");
        
        // Add the wallet balance to the model
        model.addAttribute("walletBalance", walletBalance);
    	return "ServiceProviderWelcomeScreen";
	}
    
    //Dhruv
    @GetMapping("/addServiceForm")
    public String addServiceForm(Model model){
    	model.addAttribute(SEARCH_SERVICE,new SearchService());
    	return "addServiceForm";
    }
    
    //Dhruv
    @PostMapping("/addServiceItem")
    @ResponseBody
    public String addServiceItem(SearchService searchService) {
    	int s = serviceproviderservice.verifyServiceAddition(searchService);
        if (s == -1) {
            return "You can only add one service per ServiceProvider."; // Or redirect to an appropriate page with a message
        }
    	return "Service Added Sucesfully!";
    }

    //Achyutam
    @GetMapping("/ViewBooking")
    public String viewBooking(Model model) {
        List<ServiceProviderBookingDTO> bookings = serviceproviderservice.getBookedServices();
        model.addAttribute("ServiceProviderBookingDTO", bookings);
        return "ServiceProviderBookedServices";
    }



    //Achyutam
    @GetMapping("/PastBookings")
    public String viewPastBookings(Model model) {
        List<ServiceProviderBookingDTO> pastBookings = serviceproviderservice.getPastBookings();
        model.addAttribute("pastBookings", pastBookings);
        return "PastBooking";
    }

    //Dhruv
    @GetMapping("/ListServices")
    public String listServices(Model model){
    	System.out.println(globalcontext.getServiceProviderId());
    	model.addAttribute(SEARCH_SERVICE,serviceproviderservice.getAllServiceProviderServices());
    	return "ListServices";
    }
    

    //Dhruv
    @PostMapping("/DeleteService/{providerName}/{serviceId}")
    public RedirectView deleteService(@PathVariable("providerName") String providerName,@PathVariable("serviceId") String serviceId, Model model) {
    		serviceproviderservice.deleteSelectedService(providerName,serviceId);
        	return new RedirectView("/ServiceProvider/ListServices");
    }

    //Dhruv
    // Endpoint to modify a service
    @PostMapping("/ModifyService/{providerName}/{serviceId}")
    public String modifyService(@PathVariable("providerName") String providerName,@PathVariable("serviceId") String serviceId , Model model) {
        // Fetch the service details and populate the modification form
        List<SearchService> res = serviceproviderservice.getServiceForModify(providerName,serviceId);
        //SearchService service = res.get(0);
        model.addAttribute(SEARCH_SERVICE, res.get(0));
        System.out.println(res.get(0));
        return "ModifyService"; // Name of the Thymeleaf template for modification
    }
    

    //Dhruv
    @PostMapping("/SaveModifiedService")
    public RedirectView saveModifiedService(@ModelAttribute SearchService searchService, Model model) {
    	serviceproviderservice.updateService(searchService);
        return new RedirectView("/ServiceProvider/ListServices");
    }

    //Achyutam
    @PostMapping("/UpdateBookingStatus")
    public RedirectView updateBookingStatus(@RequestParam String bookingId, @RequestParam String status, RedirectAttributes redirectAttributes) {
        try {
            serviceproviderservice.updateBookingStatus(bookingId, status);

            //Dhruv Design Pattern
            List<ServiceProvider> res = serviceproviderservice.getServiceProviderByServiceId();
            List<Customer> customers = customerService.getAllCustomersByCity(res.get(0).getCity());

            serviceProviderStateManager.changeState(res.get(0), status, customers);

            redirectAttributes.addFlashAttribute("message", "Booking status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update booking status.");
        }
        return new RedirectView("/ServiceProvider/ViewBooking");
    }









}