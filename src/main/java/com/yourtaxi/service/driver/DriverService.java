package com.yourtaxi.service.driver;

import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.OnlineStatus;
import com.yourtaxi.exception.CarAlreadyInUseException;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.DriverOfflinePickCarException;
import com.yourtaxi.exception.EntityNotFoundException;

import java.util.List;

public interface DriverService
{

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    List<DriverDO> find(OnlineStatus onlineStatus);

    DriverDO selectCar(Long driveId, Long carId) throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException;

    DriverDO updateDriverStatus(Long driveId) throws EntityNotFoundException;

    List<DriverDO> findAll();

}
