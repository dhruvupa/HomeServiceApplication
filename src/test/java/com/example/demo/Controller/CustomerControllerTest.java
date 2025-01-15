package com.example.demo.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.Model.Booking.Booking;
import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Service.Customer.CustomerService;
import com.example.demo.Service.SearchService.SearchServiceService;
import com.example.demo.Service.WalletService.WalletService;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;
    
    @Mock
    private WalletService walletService;

    @Mock
    private Model model;

    @Mock
    private IUserFactory userFactory;

    @Mock
    private SearchServiceService searchServiceService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController.setGlobalCustomername("testCustomer");
    }

    @Test
    void testCheckUser() {
        // Arrange
        when(userFactory.createUser("Customer")).thenReturn(new Customer());

        // Act
        String viewName = customerController.checkUser(model);

        // Assert
        verify(model).addAttribute(eq("customer"), any(Customer.class));
        assertEquals("CustomerRegistration", viewName);
    }

    @Test
    void testCreateCustomer() {
        // Arrange
    	Customer customer = new Customer();
        customer.setId(1); // Assuming a default ID for the customer

        // Mocking the service calls
        when(customerService.addCustomer(customer)).thenReturn(customer);  // Assume the customer is created successfully
        doNothing().when(walletService).createWallet(anyInt(), eq("CUSTOMER"), eq(100.0f)); // Mock createWallet to do nothing

        // Act
        String response = customerController.createCustomer(customer);

        // Assert
        assertEquals("User Created Successfully", response);
        verify(customerService).addCustomer(customer);  // Verify that the service method was called
        verify(walletService).createWallet(customer.getId(), "CUSTOMER", 100.0f);
    }

    @Test
    void testAuthenticateCustomer_Success() {
        // Arrange
        String name = "testName";
        String password = "testPassword";
        when(customerService.checkCustomerLogin(name, password)).thenReturn(1);

        // Act
        String viewName = customerController.authenticateCustomer(name, password, model);

        // Assert
        assertEquals("CustomerWelcomeScreen", viewName);
    }

    @Test
    void testAuthenticateCustomer_Failure() {
        // Arrange
        String name = "testName";
        String password = "testPassword";
        when(customerService.checkCustomerLogin(name, password)).thenReturn(0);
        when(userFactory.createUser("Customer")).thenReturn(new Customer());

        // Act
        String viewName = customerController.authenticateCustomer(name, password, model);

        // Assert
        verify(model).addAttribute(eq("customer"), any(Customer.class));
        assertEquals("CustomerRegistration", viewName);
    }


    @Test
    void testRateService() {
        // Arrange
        List<Booking> bookings = new ArrayList<>();
        when(customerService.getAllPastBooking(anyString())).thenReturn(bookings);

        // Act
        String viewName = customerController.rateService(model);

        // Assert
        verify(model).addAttribute(eq("Booking"), eq(bookings));
        assertEquals("CustomerPastServices", viewName);
    }

    @Test
    void testPostService() {
        // Arrange
        int service_id = 1;
        String rating = "5";
        when(customerService.postRating(service_id, rating)).thenReturn(1);

        // Act
        String response = customerController.postService(service_id, rating);

        // Assert
        assertEquals("Rating posted", response);
    }

    @Test
    void testDeleteService() {
        // Arrange
        String bookingId = "123";
        String bookingStatus = "ACCEPTED";
        when(customerService.cancelSelectedBooking(bookingId, bookingStatus)).thenReturn(1);

        // Act
        RedirectView redirectView = customerController.deleteService(bookingId, bookingStatus, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute("message", "Booking Status Rejected Successfully!!");
        assertEquals("/Customer/CustomerCurrentBookedServices", redirectView.getUrl());
    }
}
