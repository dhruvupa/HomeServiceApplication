package com.example.demo.Model.ServiceProvider;

import com.example.demo.DAORepo.ServiceProvider_Repository;

public interface IServiceProviderStatusState {
	void handleStatusState(ServiceProvider provider,ServiceProvider_Repository repository);
    String getStateName();
}
