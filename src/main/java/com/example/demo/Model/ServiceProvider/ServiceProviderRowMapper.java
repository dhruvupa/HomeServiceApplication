package com.example.demo.Model.ServiceProvider;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderRowMapper implements RowMapper<ServiceProvider>{
	
	   
	 public ServiceProvider mapRow(ResultSet rs, int rowNum) throws SQLException {
		 ServiceProvider serviceProvider = new ServiceProvider();
		 serviceProvider.setName(rs.getString("provider_name")); 
		 serviceProvider.setCity(rs.getString("city"));
		 serviceProvider.setEmail(rs.getString("email"));
		 serviceProvider.setStatus(rs.getString("status"));
		 serviceProvider.setProviderId(rs.getString("provider_id"));		
		return serviceProvider;
	    }
}
