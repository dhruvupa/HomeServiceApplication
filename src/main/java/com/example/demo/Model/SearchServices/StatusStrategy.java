package com.example.demo.Model.SearchServices;

import java.util.List;

public class StatusStrategy implements IFilterStrategy {
    private String status;

    public StatusStrategy(String status) {
        this.status = status;
    }

    @Override
    public String applyFilter(StringBuilder sql, List<Object> params) {
        if (status != null && !status.isEmpty()) {
            sql.append(" AND LOWER(status) = LOWER(?)");
            params.add(status);
        }
        System.out.println("Status based filter strategy");
        return sql.toString();
    }
}
