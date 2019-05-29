package com.yourtaxi.service.car.impl;

import com.yourtaxi.dataaccessobject.CarRepository;
import com.yourtaxi.dataaccessobject.ManufacturerRepository;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.domainobject.ManufacturerDO;
import com.yourtaxi.domainvalue.EngineType;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.exception.InvalidRatingValueException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCarServiceTest
{

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DefaultCarService carService;

    private CarDO car;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        car = new CarDO("123LY", 2, false, EngineType.DIESEL, new ManufacturerDO("BMW"));
        car.setId(123L);
    }


    @Test
    public void findCarSuccessfully() throws EntityNotFoundException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        Assert.assertEquals(car, carService.find(123L));
    }


    @Test(expected = EntityNotFoundException.class)
    public void tryToFindCarThatDoesNotExist() throws EntityNotFoundException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(null);
        carService.find(123L);
    }


    @Test
    public void findAll()
    {
        Mockito.when(carRepository.findAll()).thenReturn(() -> Collections.singletonList(car).iterator());
        Collection<CarDO> carDOCollection = carService.findAll();
        Assert.assertEquals(1, carDOCollection.size());
        Assert.assertTrue(carDOCollection.contains(car));
    }


    @Test
    public void createSuccessfully() throws ConstraintsViolationException
    {
        Mockito.when(manufacturerRepository.save(new ManufacturerDO("BMW"))).thenReturn(Mockito.mock(ManufacturerDO.class));
        Mockito.when(carRepository.save(car)).thenReturn(car);
        Assert.assertEquals(car, carService.create(car));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void failedToCreateCarDueToDataIntegrity() throws ConstraintsViolationException
    {
        Mockito.when(manufacturerRepository.save(new ManufacturerDO("BMW"))).thenReturn(Mockito.mock(ManufacturerDO.class));
        Mockito.when(carRepository.save(car)).thenThrow(new DataIntegrityViolationException("Field can not be null"));
        carService.create(car);
    }


    @Test
    public void deleteSuccessfully() throws EntityNotFoundException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        carService.delete(123L);
        Assert.assertEquals(true, car.getDeleted());
    }


    @Test
    public void updateSuccessfully() throws ConstraintsViolationException, EntityNotFoundException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        Mockito.when(manufacturerRepository.save(new ManufacturerDO("BMW"))).thenReturn(Mockito.mock(ManufacturerDO.class));
        Mockito.when(carRepository.save(car)).thenReturn(car);
        Assert.assertEquals(car, carService.update(123L, car));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void failedToUpdateCarDueToDataIntegrity() throws ConstraintsViolationException, EntityNotFoundException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        Mockito.when(manufacturerRepository.save(new ManufacturerDO("BMW"))).thenReturn(Mockito.mock(ManufacturerDO.class));
        Mockito.when(carRepository.save(car)).thenThrow(new DataIntegrityViolationException("Field can not be null"));
        carService.update(123L, car);
    }


    @Test
    public void rateCar() throws EntityNotFoundException, InvalidRatingValueException
    {
        car.setRating(4D);
        car.setNumberOfRatings(2L);
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        CarDO rateCar = carService.rateCar(123L, 2D);
        Assert.assertEquals(3.33D, rateCar.getRating(), 0.001);
        Assert.assertEquals(3L, rateCar.getNumberOfRatings(), 0);
    }


    @Test(expected = InvalidRatingValueException.class)
    public void rateCarWithBelow0Rate() throws EntityNotFoundException, InvalidRatingValueException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        carService.rateCar(123L, -2D);
    }


    @Test(expected = InvalidRatingValueException.class)
    public void rateCarGreaterThan5Rate() throws EntityNotFoundException, InvalidRatingValueException
    {
        Mockito.when(carRepository.findOne(123L)).thenReturn(car);
        carService.rateCar(123L, 20D);
    }
}
