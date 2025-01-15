package com.example.demo.Service.Payment;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.Payment.*;
import com.example.demo.DAORepo.PaymentRepository;
import com.example.demo.Model.ServiceProvider.ServiceProvider;
import com.example.demo.Service.OTP.OTPService;
import com.example.demo.DAORepo.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService
{

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OTPService otpService;
    
    @Autowired
    private WalletRepository walletRepository;
    
    public PaymentService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    private int getcount = 0;
    private String finalmessage="";

	public String processFinalPayment(BasePayment payment, Customer customer, String serviceId, ServiceProvider serviceProvider)
    {
        payment.addObserver(customer,serviceProvider);

        // Check booking count for customer to decide on discounts
    	Map<String,Object> count;

    	List<Map<String, Object>> bookingCount = paymentRepository.getBookingCountByCustomerId(customer.getId());
    	if (!bookingCount.isEmpty())
    	{
	        count=bookingCount.get(0);
	        getcount=((Number) count.get("COUNT(*)")).intValue();
    	}
        System.out.println(getcount);
        
        if (getcount > 4) {
            payment = new LoyaltyPointsDecorator(payment);
           
        } else if (getcount >=2 && getcount<=4) {
        	System.out.println("inside discount decorator");
            payment = new DiscountDecorator(payment);
        }

        // Process payment
        float finalamount = payment.processPayment();
        System.out.println(finalamount);
        
        // Wallet Logic: Fetch customer and service provider wallet balances
        float customerBalance = walletRepository.getWalletBalanceByUserId(customer.getId(), "CUSTOMER");
        int serviceProviderId = paymentRepository.getServiceProviderIdByServiceId(serviceId); // Assuming this method exists
        float serviceProviderBalance = walletRepository.getWalletBalanceByUserId(serviceProviderId, "SERVICE_PROVIDER");


        // Check if customer has sufficient balance
        if (customerBalance < finalamount) {
            throw new RuntimeException("Insufficient wallet balance. Please add funds to your wallet.");
        }
        
        // Deduct from customer wallet and add to service provider wallet
        walletRepository.updateWalletBalance(customer.getId(), "CUSTOMER", customerBalance - finalamount);
        walletRepository.updateWalletBalance(serviceProviderId, "SERVICE_PROVIDER" ,serviceProviderBalance + finalamount);

        
        // Generate a timestamp for the payment
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        payment.setTimestamp(timestamp);

        // Create a new booking after successful payment
        String bookingId = paymentRepository.createBooking(customer.getId(), serviceId);
        payment.setBookingId(bookingId);


        // Save payment in the database
        paymentRepository.savePayment(bookingId, finalamount, "COMPLETED", payment.getTimestamp());


        String otpCode = otpService.generateAndStoreOTP(bookingId);

        // Update booking payment status
        paymentRepository.updateBookingPaymentStatus(bookingId);
        
        //Letting customer know if they have got any discount or not
        if (getcount > 4) {
            finalmessage="Final amount after 10% loyalty discount: " + finalamount + " Booking ID: " + bookingId + " OTP Code: " + otpCode;
        } else if (getcount >=2 && 4<=getcount) {
        	finalmessage="Final amount after 5% discount: " + finalamount + " Booking ID: " + bookingId + " OTP Code: " + otpCode;
        }
        else
        {
        	// + otpCode
        	finalmessage="Final amount: " + finalamount + " Booking ID: " + bookingId + " OTP Code: " + otpCode;
        }
        return finalmessage;
    }
}