package com.example.demo.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Model.Customer.Customer;
import com.example.demo.Model.ServiceProvider.ServiceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.Model.Booking.ServiceProviderBookingDTO;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.ServiceProvider.ServiceProviderStateManager;
import com.example.demo.Model.User.IUserFactory;
import com.example.demo.Service.Customer.CustomerService;
import com.example.demo.Service.ServiceProvider.GlobalContext;
import com.example.demo.Service.ServiceProvider.ServiceProviderService;
import com.example.demo.Service.WalletService.WalletService;


class ServiceProvider_ControllerTest {

	@InjectMocks
	private ServiceProviderController serviceProviderController;
	
	@Mock
	private ServiceProviderService serviceproviderservice;
	
	@Mock
	private IUserFactory userFactory;
	
	@Mock
	private GlobalContext globalcontext;
	
	@Mock
	private WalletService walletService;
	
	@Mock
	private ServiceProviderStateManager serviceProviderStateManager;
	
	@Mock
	private CustomerService customerService;

	@Mock
	private Model model;

	@Mock
	private RedirectAttributes redirectAttributes;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testLoginForm() {
		String viewName = serviceProviderController.loginForm(model);
		assertEquals("ServiceProvider_login", viewName);
	}

	@Test
	void testLoginServiceProvider() {
		String email = "test@gmail.com";
		String correctPassword = "1234";
		String wrongPassword = "12341";
		RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
		when(serviceproviderservice.validateLogin(email, correctPassword)).thenReturn(true);
		RedirectView result = serviceProviderController.loginServiceProvider(email, correctPassword,
				redirectAttributes);

		assertThat(result).isNotNull();
		assertThat(result.getUrl()).isEqualTo("ServiceProviderWelcomeScreen");

		when(serviceproviderservice.validateLogin(email, wrongPassword)).thenReturn(false);
		RedirectView result2 = serviceProviderController.loginServiceProvider(email, wrongPassword,
				redirectAttributes);

		assertThat(result2).isNotNull();
		assertThat(result2.getUrl()).isEqualTo("ServiceProviderLoginForm");
	}

	@Test
	void testServiceProviderForm() {
		
		when(globalcontext.getServiceProviderId()).thenReturn("1");
		when(walletService.getWalletBalanceByUserId(1, "SERVICE_PROVIDER")).thenReturn((float) 100);
		
		String view = serviceProviderController.serviceProviderForm(model);
		
		assertEquals("ServiceProviderWelcomeScreen", view);
	}
	
	@Test
	void testAddServiceForm() {
		String view = serviceProviderController.addServiceForm(model);
		
		assertEquals("addServiceForm", view);
	}
	
	@Test
	void testAddServiceItem() {
		SearchService searchService =new SearchService();
		when(serviceproviderservice.verifyServiceAddition(searchService)).thenReturn(-1);
		String result = serviceProviderController.addServiceItem(searchService);
		
		when(serviceproviderservice.verifyServiceAddition(searchService)).thenReturn(1);
		String result2 = serviceProviderController.addServiceItem(searchService);
		
		assertEquals("You can only add one service per ServiceProvider.", result);
		assertEquals("Service Added Sucesfully!", result2);
	}

	@Test
	void testViewBooking() {
		 ServiceProviderBookingDTO serviceProviderBookingDTO = new ServiceProviderBookingDTO();
		 serviceProviderBookingDTO.setCustomerName("Test");
		 List<ServiceProviderBookingDTO> bookings = new ArrayList<>();
		 bookings.add(serviceProviderBookingDTO);
		 
		 when(serviceproviderservice.getBookedServices()).thenReturn(bookings);
		 String view =  serviceProviderController.viewBooking(model);
		 
		 assertEquals("ServiceProviderBookedServices", view);
	}

	@Test
    void testViewPastBooking()
	{
		ServiceProviderBookingDTO serviceProviderBookingDTO = new ServiceProviderBookingDTO();
		List<ServiceProviderBookingDTO> pastBookings = new ArrayList<>();
		pastBookings.add(serviceProviderBookingDTO);

		when(serviceproviderservice.getPastBookings()).thenReturn(pastBookings);
		String view =  serviceProviderController.viewPastBookings(model);
		
		assertEquals("PastBooking", view);

	}

	@Test
	void testListServices()
	{
		when(globalcontext.getServiceProviderId()).thenReturn(String.valueOf(1));
		String view = serviceProviderController.listServices(model);
		
		assertEquals("ListServices", view);

	}

	@Test
	void testdeleteService()
	{
		when(serviceproviderservice.deleteSelectedService("John", "1")).thenReturn(1);
		RedirectView view = serviceProviderController.deleteService("John", "1", model);
		//assertThat(view).isNotNull();
		assertThat(view.getUrl()).isEqualTo("/ServiceProvider/ListServices");

	}

	@Test
	void testmodifyService()
	{
		SearchService searchService = new SearchService();
		searchService.setProviderId(1);

		List<SearchService> searchServicesList = new ArrayList<>();
		searchServicesList.add(searchService);

		when(serviceproviderservice.getServiceForModify("name", "1")).thenReturn(searchServicesList);
		String view = serviceProviderController.modifyService("name", "1", model);
		
		assertEquals("ModifyService", view);

	}

	@Test
	void testsavemodifiedservices()
	{
		SearchService searchService = new SearchService();
		when(serviceproviderservice.updateService(searchService)).thenReturn(1);
		RedirectView view = serviceProviderController.saveModifiedService(searchService, model);
		
		assertThat(view.getUrl()).isEqualTo("/ServiceProvider/ListServices");
	}

	@Test
	void testupdateBookingStatus()
	{
		ServiceProvider serviceProvider = new ServiceProvider();
		Customer customer = new Customer();

		List<ServiceProvider> serviceProviderList = new ArrayList<>();
		serviceProviderList.add(serviceProvider);
		List<Customer> customerList = new ArrayList<>();
		customerList.add(customer);

		doNothing().when(serviceproviderservice).updateBookingStatus("1", "Pending");
		doNothing().when(serviceProviderStateManager).changeState(serviceProvider,"Booking", customerList);
		when(serviceproviderservice.getServiceProviderByServiceId()).thenReturn(serviceProviderList);

		RedirectView view = serviceProviderController.updateBookingStatus("1", "Pending",redirectAttributes);
		assertThat(view.getUrl()).isEqualTo("/ServiceProvider/ViewBooking");


	}
	
}
