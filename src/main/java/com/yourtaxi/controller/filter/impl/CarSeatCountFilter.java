package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;

import java.util.List;
import java.util.stream.Collectors;

public class CarSeatCountFilter implements Filter
{
    private SearchCriteria searchCriteria;


    public CarSeatCountFilter(SearchCriteria searchCriteria)
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
                    (driver.getCar() != null && driver.getCar().getSeatCount() < Integer.parseInt(searchCriteria.getValue().toString()))
                ).collect(Collectors.toList());
            case ">":
                return drivers.stream().filter(driver ->
                    driver.getCar() != null &&
                        driver.getCar().getSeatCount() > Integer.parseInt(searchCriteria.getValue().toString())
                ).collect(Collectors.toList());
            default:
                return drivers.stream().filter(driver ->
                    driver.getCar() != null && driver.getCar().getSeatCount() == Integer.parseInt(searchCriteria.getValue().toString())
                ).collect(Collectors.toList());
        }
    }
}
