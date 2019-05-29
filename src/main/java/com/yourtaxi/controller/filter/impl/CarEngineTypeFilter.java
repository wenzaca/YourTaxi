package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.EngineType;

import java.util.List;
import java.util.stream.Collectors;

public class CarEngineTypeFilter implements Filter
{
    private SearchCriteria searchCriteria;


    public CarEngineTypeFilter(SearchCriteria searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }


    @Override
    public List<DriverDO> execute(List<DriverDO> drivers)
    {
        if (searchCriteria == null)
        {
            return drivers;
        }
        return drivers.stream()
            .filter(driver -> driver.getCar() != null && EngineType.valueOf(searchCriteria.getValue().toString().toUpperCase()).equals(driver.getCar().getEngineType())
            ).collect(Collectors.toList());
    }
}
