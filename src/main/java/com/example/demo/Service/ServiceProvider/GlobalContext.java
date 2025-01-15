package com.example.demo.Service.ServiceProvider;

import org.springframework.stereotype.Component;

@Component
public class GlobalContext {
	private volatile String serviceProviderId;

    public  String getServiceProviderId() {
        return serviceProviderId;
    }

    public synchronized void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public synchronized void clear() {
        this.serviceProviderId = null;
    }
}
