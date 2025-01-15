package com.example.demo.Model.ServiceProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.demo.DAORepo.ServiceProvider_Repository;
import com.example.demo.Model.Customer.Customer;

@Component
public class ServiceProviderStateManager {

	private final AvailableStatusState availableState;

	private final BusyStatusState busyState;

	private final ServiceProvider_Repository serviceProviderRepository;

	public ServiceProviderStateManager(AvailableStatusState availableState, BusyStatusState busyState,
			ServiceProvider_Repository serviceProviderRepository) {
		this.serviceProviderRepository = serviceProviderRepository;
		this.busyState = busyState;
		this.availableState = availableState;
	}

	public void changeState(ServiceProvider provider, String stateName, List<Customer> customers) {

		for (Customer customer : customers) {
			provider.addObserver(customer);
		}

		switch (stateName.toUpperCase()) {
		case "COMPLETED":
			// availableState.updateServicesStatus(provider, serviceProviderRepository);
			provider.setState(availableState);
			break;
		case "ACCEPTED":
			// busyState.updateServicesStatus(provider, serviceProviderRepository);
			provider.setState(busyState);
			break;
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
		provider.handleState(serviceProviderRepository);
	}

}
