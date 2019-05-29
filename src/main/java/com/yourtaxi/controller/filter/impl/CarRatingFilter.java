package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;

import java.util.List;
import java.util.stream.Collectors;

public class CarRatingFilter implements Filter
{
    private SearchCriteria searchCriteria;


    public CarRatingFilter(SearchCriteria searchCriteria)
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
        switch (searchCriteria.getOperation())
        {
            case "<":
                return drivers.stream().filter(driver ->
                    driver.getCar() != null && driver.getCar().getRating() < Double.parseDouble(searchCriteria.getValue().toString())
                ).collect(Collectors.toList());
            case ">":
                return drivers.stream().filter(driver -> driver.getCar() != null && driver.getCar().getRating() > Double.parseDouble(searchCriteria.getValue().toString())
                ).collect(Collectors.toList());
            default:
                return drivers.stream().filter(driver -> driver.getCar() != null && driver.getCar().getRating().equals(Double.parseDouble(searchCriteria.getValue().toString()))
                ).collect(Collectors.toList());
        }
    }
}
