package com.example.demo.Model.SearchServices;

import java.util.List;

public class CityStrategy implements IFilterStrategy{
	private String city;
    public CityStrategy(String city) {
        this.city = city;
    }

    @Override
    public String applyFilter(StringBuilder sql, List<Object> params) {
        if (city != null && !city.isEmpty()) {
            sql.append(" AND LOWER(city) = LOWER(?)");
            params.add(city);
        }
        System.out.println("City based filter strategy");
        return sql.toString();
    }
    
}
