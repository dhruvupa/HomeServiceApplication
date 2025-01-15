package com.example.demo.Model.SearchServices;

import java.util.List;

public interface IFilterStrategy {
	String applyFilter(StringBuilder sql, List<Object> params);
}
