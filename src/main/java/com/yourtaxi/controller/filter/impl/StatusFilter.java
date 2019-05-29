package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.OnlineStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StatusFilter implements Filter
{

    private SearchCriteria searchCriteria;


    public StatusFilter(SearchCriteria searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }


    @Override public List<DriverDO> execute(List<DriverDO> drivers)
    {
        if (searchCriteria == null)
        {
            return drivers;
        }
        return drivers.stream().filter(driver -> OnlineStatus.valueOf(searchCriteria.getValue().toString().toUpperCase()).equals(driver.getOnlineStatus()))
            .collect(Collectors.toList());
    }
}
