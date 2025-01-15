package com.example.demo.Model.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.Model.ServiceProvider.ServiceProvider;

@Component
public class ServiceProviderRowMapperForAdmin implements RowMapper<ServiceProvider>{
	   
	 public ServiceProvider mapRow(ResultSet rs, int rowNum) throws SQLException {
		 ServiceProvider serviceProvider = new ServiceProvider();
		 serviceProvider.setName(rs.getString("name")); 
		 serviceProvider.setCity(rs.getString("city"));
		 serviceProvider.setEmail(rs.getString("email")); 
		 serviceProvider.setId(rs.getInt("provider_id"));		
		return serviceProvider;
	    }
}