package com.yourtaxi.service.driver.impl;

import com.yourtaxi.dataaccessobject.DriverRepository;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.GeoCoordinate;
import com.yourtaxi.domainvalue.OnlineStatus;
import com.yourtaxi.exception.CarAlreadyInUseException;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.DriverOfflinePickCarException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.service.car.CarService;
import com.yourtaxi.service.driver.DriverService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private DriverRepository driverRepository;

    private CarService carService;


    public DefaultDriverService(final DriverRepository driverRepository, final CarService carService)
    {

        this.carService = carService;
        this.driverRepository = driverRepository;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId primary key for searching
     * @return found driver
     * @throws EntityNotFoundException Entity not found by that Id
     */
    @Override
    @Cacheable("cars")
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {

        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO driver to be persisted
     * @return persisted driver
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {

        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to driver creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId primary key for searching
     * @throws EntityNotFoundException Entity not found by that Id
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {

        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCar(null);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId  primary key for searching
     * @param longitude longitude data
     * @param latitude  latitude data
     * @throws EntityNotFoundException Entity not found by that Id
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {

        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus driver status
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {

        return driverRepository.findByOnlineStatus(onlineStatus);
    }


    /**
     * Enable drivers to select a car
     *
     * @param driveId primary key for searching the driver
     * @param carId   primary key for searching the car
     * @return driver with selected car
     * @throws EntityNotFoundException  Entity not found by that Id
     * @throws CarAlreadyInUseException if the car selected is already in use
     */
    @Override
    @Transactional
    public DriverDO selectCar(Long driveId, Long carId)
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        if (carId == null)
        {
            return deselectCar(driveId);
        }
        CarDO carDO = carService.find(carId);
        if (!driverRepository.findByCarId(carId)
            .isEmpty())
        {
            throw new CarAlreadyInUseException("The select car is already in use");
        }

        DriverDO driverDO = this.findDriverChecked(driveId);
        if (OnlineStatus.OFFLINE.equals(driverDO.getOnlineStatus()))
        {
            throw new DriverOfflinePickCarException("The driver must be online to pick a car");
        }
        driverDO.setCar(carDO);
        return driverDO;
    }


    /**
     * Enable drivers to deselect a car
     *
     * @param driveId primary key for searching
     * @return driver with deselected car
     * @throws EntityNotFoundException Entity not found by that Id
     */
    private DriverDO deselectCar(Long driveId) throws EntityNotFoundException
    {

        DriverDO driverDO = this.find(driveId);
        driverDO.setCar(null);
        return driverDO;
    }


    /**
     * Turnover drive online status status
     *
     * @param driveId primary key for searching
     * @return updated driver
     * @throws EntityNotFoundException Entity not found by that Id
     */
    @Override
    @Transactional
    @Cacheable("drivers")
    public DriverDO updateDriverStatus(Long driveId) throws EntityNotFoundException
    {

        DriverDO driverDO = this.find(driveId);
        if (OnlineStatus.ONLINE.equals(driverDO.getOnlineStatus()))
        {
            driverDO.setOnlineStatus(OnlineStatus.OFFLINE);
            driverDO.setCar(null);
        }
        else
        {
            driverDO.setOnlineStatus(OnlineStatus.ONLINE);
        }
        return driverDO;
    }


    /**
     * Select filtered drivers
     *
     * @return all drivers
     */
    @HystrixCommand
    @Cacheable("drivers")
    public List<DriverDO> findAll()
    {

        return StreamSupport.stream(this.driverRepository.findAll()
            .spliterator(), true)
            .collect(Collectors.toList());

    }


    /**
     * This method is use to get a car by Id
     *
     * @param driverId primary key for searching
     * @return found driver by id
     * @throws EntityNotFoundException Entity not found by that Id
     */
    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {

        DriverDO driverDO = driverRepository.findOne(driverId);
        if (driverDO == null)
        {
            throw new EntityNotFoundException("Could not find Driver entity with id: " + driverId);
        }
        return driverDO;
    }

}
