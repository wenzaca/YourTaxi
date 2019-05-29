package com.yourtaxi.controller.filter;

import com.yourtaxi.controller.filter.criteria.SearchCriteria;
import com.yourtaxi.controller.filter.impl.*;
import com.yourtaxi.domainobject.DriverDO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChainFilter
{
    private List<Filter> filters = new LinkedList<>();


    public void addFilters(List<SearchCriteria> searchCriteriaList)
    {
        Map<String, SearchCriteria> keySearchCriteriaMap = searchCriteriaList.stream().collect(Collectors.toMap(SearchCriteria::getKey, Function.identity()));
        filters.add(new CoordinateFilter(keySearchCriteriaMap.get("coordinate")));
        filters.add(new StatusFilter(keySearchCriteriaMap.get("onlineStatus")));
        filters.add(new UsernameFilter(keySearchCriteriaMap.get("username")));
        filters.add(new CarIdFilter(keySearchCriteriaMap.get("car.id")));
        filters.add(new CarManufacturerFilter(keySearchCriteriaMap.get("car.manufacturer")));
        filters.add(new CarRatingFilter(keySearchCriteriaMap.get("car.rating")));
        filters.add(new CarConvertibleFilter(keySearchCriteriaMap.get("car.convertible")));
        filters.add(new CarSeatCountFilter(keySearchCriteriaMap.get("car.seatCount")));
        filters.add(new CarEngineTypeFilter(keySearchCriteriaMap.get("car.engineType")));
    }


    public List<DriverDO> filter(List<DriverDO> drivers)
    {
        for (Filter filter : filters)
        {
            drivers = filter.execute(drivers);
        }
        return drivers;
    }
}
