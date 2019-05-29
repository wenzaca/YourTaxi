package com.yourtaxi.service.car.impl;

import com.yourtaxi.dataaccessobject.CarRepository;
import com.yourtaxi.dataaccessobject.ManufacturerRepository;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.exception.InvalidRatingValueException;
import com.yourtaxi.service.car.CarService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some car specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService
{

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private CarRepository carRepository;

    private ManufacturerRepository manufacturerRepository;


    public DefaultCarService(final CarRepository carRepository, final ManufacturerRepository manufacturerRepository)
    {
        this.manufacturerRepository = manufacturerRepository;
        this.carRepository = carRepository;
    }


    /**
     * Selects a car by id.
     *
     * @param carId entity primary key
     * @return found car
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Cacheable("cars")
    public CarDO find(Long carId) throws EntityNotFoundException
    {
        return findCar(carId);
    }


    /**
     * Get All cars.
     *
     * @return found cars
     */
    @Override
    @HystrixCommand
    @Cacheable("cars")
    public List<CarDO> findAll()
    {
        return StreamSupport.stream(this.carRepository.findAll().spliterator(), true)
            .collect(Collectors.toList());
    }


    /**
     * Creates a new car.
     *
     * @param carDO car to be persisted
     * @return persisted car
     * @throws ConstraintsViolationException if a car already exists with the given username, ... .
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        return saveCarDO(carDO);
    }


    /**
     * Deletes an existing car by id.
     *
     * @param carId car primary key
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCar(carId);
        carDO.setDeleted(true);
    }


    /**
     * Update the location for a car.
     *
     * @param carDO car to be updated
     * @throws ConstraintsViolationException Missing fields
     * @throws EntityNotFoundException Entity not found by that Id
     */
    @Override
    @Transactional
    public CarDO update(Long carID, CarDO carDO) throws ConstraintsViolationException, EntityNotFoundException
    {
        findCar(carID);
        return saveCarDO(carDO);
    }


    /**
     * Method responsible to persist data
     *
     * @param carDO object to be persisted
     * @return persisted object
     * @throws ConstraintsViolationException data violation
     */
    private CarDO saveCarDO(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            manufacturerRepository.save(carDO.getManufacturer());
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to car creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    /**
     * Update the location for a car.
     *
     * @param carID car primary key
     * @param rating the car rate
     * @return rated car
     * @throws EntityNotFoundException Entity not found by that Id.
     * @throws InvalidRatingValueException if the rating value is below 0 or greater than 5
     */
    @Override
    @Transactional
    public CarDO rateCar(Long carID, Double rating) throws EntityNotFoundException, InvalidRatingValueException
    {
        CarDO carDO = findCar(carID);
        if (rating < 0 || rating > 5)
        {
            LOG.warn("The rating value must be between 0 and 5");
            throw new InvalidRatingValueException("The rating value must be between 0 and 5");
        }
        Long newRateNumber = carDO.getNumberOfRatings() + 1;
        carDO.setNumberOfRatings(newRateNumber);
        carDO.setRating(getNewRating(rating, carDO.getRating(), newRateNumber));
        return carDO;
    }


    /**
     * Method responsible for calculating the new rating.
     *
     * @param newRating new rating that was given to the car
     * @param oldRating old average rating
     * @param newRateNumber new Rating counter, it the old rating plus one
     * @return return the new rating from the car
     */
    private double getNewRating(Double newRating, Double oldRating, Long newRateNumber)
    {
        DecimalFormat formatRating = new DecimalFormat("##.00");
        return Double.parseDouble(formatRating.format((oldRating * (newRateNumber - 1) + newRating) / (newRateNumber)).replace(",", "."));
    }


    /**
     * Find a car by it PK
     *
     * @param carId primary key.
     * @return car get by the id.
     * @throws EntityNotFoundException Entity not found by that Id.
     */
    private CarDO findCar(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = carRepository.findOne(carId);
        if (carDO == null)
        {
            throw new EntityNotFoundException("Could not find Car entity with id: " + carId);
        }
        return carDO;
    }

}
