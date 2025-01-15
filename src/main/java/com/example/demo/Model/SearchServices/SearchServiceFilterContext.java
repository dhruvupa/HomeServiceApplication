package com.example.demo.Model.SearchServices;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceFilterContext {
	 private List<IFilterStrategy> strategies = new ArrayList<>();

	    public void addStrategy(IFilterStrategy strategy) {
	        strategies.add(strategy);
	    }

	    public String applyFilters(StringBuilder sql, List<Object> params) {
	        for (IFilterStrategy strategy : strategies) {
	            strategy.applyFilter(sql, params);
	        }
	        return sql.toString();
	    }
}
