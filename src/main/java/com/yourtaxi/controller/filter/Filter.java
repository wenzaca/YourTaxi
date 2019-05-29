package com.yourtaxi.controller.filter;

import com.yourtaxi.domainobject.DriverDO;

import java.util.List;

public interface Filter
{
    List<DriverDO> execute(List<DriverDO> drivers);
}
