package com.example.demo.Controller;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.Payment.Payment;
import com.example.demo.Model.ServiceProvider.ServiceProvider;
import com.example.demo.Model.ServiceProvider.ServiceProviderStateManager;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Service.Customer.CustomerService;
import com.example.demo.Service.OTP.OTPService;
import com.example.demo.Service.Payment.*;
import com.example.demo.Service.ServiceProvider.ServiceProviderService;
import com.example.demo.Service.WalletService.WalletService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private final CustomerService customerServices;
    private final OTPService otpService;

    @Autowired
    private ServiceProviderService serviceproviderservice;
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ServiceProviderStateManager serviceProviderStateManager;
    
    @Autowired
    private WalletService walletService; // Injecting WalletService

    @Autowired
    private CustomerController customerControllergetter;

    public PaymentController(CustomerService customerServices, OTPService otpService) {
        this.customerServices = customerServices;
        this.otpService = otpService;
    }

    // Show Payment page with wallet balance
    @GetMapping("/makePayment")
    public String showPaymentPage(@RequestParam String serviceId, Model model) {
        String cusName = customerControllergetter.getGlobalCustomername();
        Customer customer = customerServices.getCustomerByName(cusName);
        int customerId = customer.getId();

        // Fetch the wallet balance of the logged-in customer
        float walletBalance = walletService.getWalletBalanceByUserId(customerId, "CUSTOMER");

        // Add serviceId and wallet balance to the model
        model.addAttribute("serviceId", serviceId);
        model.addAttribute("walletBalance", walletBalance); // Pass wallet balance to the view
        model.addAttribute("payment", new Payment(0.0f, "PENDING"));

        return "paymentForm"; // Thymeleaf template for payment form
    }

    // Process the payment and deduct from wallet
    @PostMapping("/processPayment")
    public String processPayment(@ModelAttribute Payment payment,
                                 @RequestParam String serviceId,
                                 Model model)

    {

        String cusName = customerControllergetter.getGlobalCustomername();
        Customer customer = customerServices.getCustomerByName(cusName);
        int customerId = customer.getId();

        // Observer Pattern
        // Fetch ServiceProvider Details for notifying ServiceProvider and Customer about booking confirmation
        List<ServiceProvider> serviceProvider = serviceproviderservice.getServiceProvider(serviceId);

        String message = paymentService.processFinalPayment(payment, customer, serviceId,serviceProvider.get(0));

        // Add the payment message to the model to display on the result page
        model.addAttribute("message", message);

        return "paymentResult"; // Thymeleaf template to display the result

    }
    
    // Get the balance of the end user
    @GetMapping("/getWalletBalance")
    public float getWalletBalanceByTypeAndId(String userType, int userId) {
        float walletBalance = 0.0f;

        if ("CUSTOMER".equalsIgnoreCase(userType)) {
            // Fetch the wallet balance for the customer
            walletBalance = walletService.getWalletBalanceByUserId(userId, "CUSTOMER");
        } else if ("SERVICE_PROVIDER".equalsIgnoreCase(userType)) {
            // Fetch the wallet balance for the service provider
            walletBalance = walletService.getWalletBalanceByUserId(userId, "SERVICE_PROVIDER");
        } else {
            // Invalid userType, return 0 balance
            throw new IllegalArgumentException("Invalid user type. Must be CUSTOMER or SERVICE_PROVIDER.");
        }

        return walletBalance;
    }



    //Achyutam
    // Handles GET request to show OTP form
    @GetMapping("/VerifyOTP")
    public String showOTPForm(@RequestParam("bookingId") String bookingId, Model model) {
        model.addAttribute("bookingId", bookingId);
        return "VerifyOTP"; // Thymeleaf template for OTP form
    }

    //Achyutam
    @PostMapping("/VerifyOTPService")
    public RedirectView verifyOTP(@RequestParam("bookingId") String bookingId,
                                  @RequestParam("otpCode") String otpCode,
                                  RedirectAttributes redirectAttributes) {
        boolean isVerified = otpService.verifyOTP(otpCode, bookingId);
        if (isVerified) {
            otpService.updateBookingStatusToCompleted(bookingId);

            List<ServiceProvider> res = serviceproviderservice.getServiceProviderByServiceId();
            List<Customer> customers = customerServices.getAllCustomersByCity(res.get(0).getCity());

            serviceProviderStateManager.changeState(res.get(0), "COMPLETED", customers);

            redirectAttributes.addFlashAttribute("message", "OTP Confirmed Successfully!");
            return new RedirectView("/ServiceProvider/ServiceProviderWelcomeScreen");
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid OTP. Please try again.");
            return new RedirectView("/ServiceProvider/VerifyOTP?bookingId=" + bookingId);
        }
    }


}