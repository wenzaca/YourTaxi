package com.yourtaxi.controller.filter.impl;

import com.yourtaxi.controller.filter.Filter;
import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.GeoCoordinate;
import com.yourtaxi.exception.GeoCoordinateCreationException;

import java.util.List;
import java.util.stream.Collectors;

public class CoordinateFilter implements Filter
{
    private SearchCriteria searchCriteria;


    public CoordinateFilter(SearchCriteria searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }


    @Override public List<DriverDO> execute(List<DriverDO> drivers)
    {
        if (searchCriteria == null)
        {
            return drivers;
        }
        GeoCoordinate geoCoordinate;
        try
        {
            geoCoordinate =
                new GeoCoordinate(
                    Double.parseDouble(searchCriteria.getValue().toString().split("\\^")[0]), Double.parseDouble(searchCriteria.getValue().toString().split("\\^")[1]));
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw new GeoCoordinateCreationException("The expected search for coordinate must be latitude value, longitude");
        }
        return drivers.stream().filter(driver ->

            geoCoordinate.equals(driver.getCoordinate())
        ).collect(Collectors.toList());
    }
}
