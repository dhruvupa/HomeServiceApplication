package com.example.demo.Service.SearchService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAORepo.SearchServiceRepository;
import com.example.demo.Model.SearchServices.CityStrategy;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.SearchServices.SearchServiceFilterContext;
import com.example.demo.Model.SearchServices.SkillStrategy;
import com.example.demo.Model.SearchServices.StatusStrategy;

@Service
public class SearchServiceService {

    @Autowired
    private SearchServiceRepository searchServiceRepository;

    //view all service when customer first logs in
    public List<SearchService> getAllServices() {
        return searchServiceRepository.findAllServices();
    }
    
    //Service based on filter
    public List<SearchService> filterServices(String skill, String city, String status) {
        StringBuilder sql = new StringBuilder("SELECT ss.service_id, ss.provider_id, ss.skill, ss.rating, ss.price, ss.status, " +
                                              "sp.name AS provider_name, sp.city, ss.category " +
                                              "FROM searchservice ss " +
                                              "JOIN serviceprovider sp ON ss.provider_id = sp.provider_id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        SearchServiceFilterContext filterContext = new SearchServiceFilterContext();

        // Add strategies based on non-null and non-empty values
        if (skill != null && !skill.isEmpty()) {
            filterContext.addStrategy(new SkillStrategy(skill));
        }
        if (city != null && !city.isEmpty()) {
            filterContext.addStrategy(new CityStrategy(city));
        }
        if (status != null && !status.isEmpty()) {
            filterContext.addStrategy(new StatusStrategy(status));
        }

        // Apply all filters to the query
        filterContext.applyFilters(sql, params);

        // Execute the query with the dynamically generated SQL and parameters
        return searchServiceRepository.queryfilterServices(sql.toString(), params.toArray());
    }
}
