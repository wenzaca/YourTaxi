package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;

import java.util.List;
import java.util.stream.Collectors;

public class UsernameFilter implements Filter
{
    private SearchCriteria searchCriteria;


    public UsernameFilter(SearchCriteria searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }


    @Override public List<DriverDO> execute(List<DriverDO> drivers)
    {
        if (searchCriteria == null)
        {
            return drivers;
        }

        return drivers.stream().filter(driver -> driver.getUsername().contains(searchCriteria.getValue().toString())).collect(Collectors.toList());
    }
}
