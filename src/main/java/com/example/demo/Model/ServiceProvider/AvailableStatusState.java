package com.example.demo.Model.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.DAORepo.ServiceProvider_Repository;

@Component
public class AvailableStatusState implements IServiceProviderStatusState{
	@Autowired
	ServiceProvider_Repository serviceprovider_repository;


    @Override
    public void handleStatusState(ServiceProvider provider,ServiceProvider_Repository repository) {
    	 if (repository != null) {
             repository.updateServicsStatus(provider); // Use the repository only if available
         }
    }

    @Override
    public String getStateName() {
        return "AVAILABLE";
    }
    
    
}
