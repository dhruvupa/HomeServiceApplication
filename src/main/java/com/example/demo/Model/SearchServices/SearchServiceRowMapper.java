package com.example.demo.Model.SearchServices;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class SearchServiceRowMapper implements RowMapper<SearchService> {
    public SearchService mapRow(ResultSet rs, int rowNum) throws SQLException {
        SearchService searchService = new SearchService();
        searchService.setServiceId(rs.getInt("service_id"));
        searchService.setProviderId(rs.getInt("provider_id"));
        searchService.setSkill(rs.getString("skill"));
        searchService.setRating(rs.getFloat("rating"));
        searchService.setPrice(rs.getFloat("price"));
        searchService.setStatus(rs.getString("status"));
        searchService.setProviderName(rs.getString("provider_name"));
        searchService.setCity(rs.getString("city"));
        searchService.setCategory(rs.getString("category"));
        return searchService;
    }
}

