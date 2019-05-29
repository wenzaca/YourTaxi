package com.yourtaxi.service.car;

import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.exception.InvalidRatingValueException;

import java.util.Collection;

public interface CarService
{

    CarDO find(Long carId) throws EntityNotFoundException;

    Collection<CarDO> findAll();

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    void delete(Long carId) throws EntityNotFoundException;

    CarDO update(Long carId, CarDO carDO) throws EntityNotFoundException, ConstraintsViolationException;

    CarDO rateCar(Long carId, Double rate) throws EntityNotFoundException, InvalidRatingValueException;
}
