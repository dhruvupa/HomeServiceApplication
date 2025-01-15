package com.example.demo.DAORepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.Model.SearchServices.*;
import com.example.demo.Model.SearchServices.SearchService;

@Repository
public class SearchServiceRepository {

    @Autowired
    private JdbcTemplate queryTemplate;

    public List<SearchService> findAllServices() {
        //String sql = "SELECT * FROM searchservice";
        String query="SELECT ss.service_id, ss.provider_id, ss.skill,  ss.rating, ss.price, ss.status, "+
        		"sp.name AS provider_name, sp.city, ss.category "+
        		"FROM searchservice ss "+
        		"JOIN serviceprovider sp ON ss.provider_id = sp.provider_id";
        return queryTemplate.query(query, new SearchServiceRowMapper());
    }
    
    @SuppressWarnings("deprecation")
	public List<SearchService> queryfilterServices(String sql, Object[] params) {
        return queryTemplate.query(sql, params, new SearchServiceRowMapper());
    }
}

