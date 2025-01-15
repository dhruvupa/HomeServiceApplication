package com.example.demo.Model.SearchServices;

import java.util.List;

public class SkillStrategy implements IFilterStrategy{

	private String skill;

    public SkillStrategy(String skill) {
        this.skill = skill;
    }

	@Override
	public String applyFilter(StringBuilder sql, List<Object> params) {
		if (skill != null && !skill.isEmpty()) {
            sql.append(" AND LOWER(skill) = LOWER(?)");
            params.add(skill);
        }
		System.out.println("Skill based filter strategy");
        return sql.toString();
	}
}
