package com.yourtaxi.controller.filter.criteria;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DriverCriteriaSearchBuilder
{
    private final List<SearchCriteria> params;


    public DriverCriteriaSearchBuilder()
    {
        params = new ArrayList<>();
    }


    public DriverCriteriaSearchBuilder with(String key, String operation, Object value)
    {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }


    public List<SearchCriteria> build()
    {
        return params;
    }
}
