package com.yourtaxi.controller;

import com.yourtaxi.controller.mapper.CarMapper;
import com.yourtaxi.datatransferobject.CarDTO;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.domainobject.ManufacturerDO;
import com.yourtaxi.domainvalue.EngineType;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.exception.InvalidRatingValueException;
import com.yourtaxi.service.car.impl.DefaultCarService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CarControllerTest
{

    @Mock
    private DefaultCarService service;

    @InjectMocks
    private CarController controller;

    private CarDO car;

    private CarDTO carDTO;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        car = new CarDO("123LY", 2, false, EngineType.DIESEL, new ManufacturerDO("BMW"));
        car.setId(123L);
        carDTO = CarMapper.makeCarDTO(car);
    }


    @Test
    public void getCarSuccessfully() throws EntityNotFoundException
    {
        Mockito.when(service.find(123L)).thenReturn(car);
        ResponseEntity<?> controllerCar = controller.getCar(123L);
        Assert.assertEquals(HttpStatus.OK, controllerCar.getStatusCode());
        Assert.assertEquals(carDTO.getId(), ((CarDTO) controllerCar.getBody()).getId());
    }


    @Test
    public void failToGetCar() throws EntityNotFoundException
    {
        Mockito.when(service.find(123L)).thenThrow(new EntityNotFoundException("Failed to find the car"));
        ResponseEntity<?> controllerCar = controller.getCar(123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerCar.getStatusCode());
        Assert.assertTrue(((String) controllerCar.getBody()).contains("Failed to find the car"));
    }


    @Test
    public void createCarSuccessFully() throws ConstraintsViolationException
    {
        Mockito.when(service.create(Matchers.any(CarDO.class))).thenReturn(car);
        ResponseEntity<?> controllerCar = controller.createCar(carDTO);
        Assert.assertEquals(HttpStatus.CREATED, controllerCar.getStatusCode());
        Assert.assertEquals(carDTO.getId(), ((CarDTO) controllerCar.getBody()).getId());
    }


    @Test
    public void failToCreateCar() throws ConstraintsViolationException
    {
        Mockito.when(service.create(Matchers.any(CarDO.class))).thenThrow(new ConstraintsViolationException("Missing fields"));
        ResponseEntity<?> controllerCar = controller.createCar(carDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerCar.getStatusCode());
        Assert.assertTrue(((String) controllerCar.getBody()).contains("Missing fields"));
    }


    @Test
    public void deleteCarSuccessfully() throws EntityNotFoundException
    {

        Mockito.doNothing().when(service).delete(123L);
        ResponseEntity<?> controllerCar = controller.deleteCar(123L);
        Assert.assertEquals(HttpStatus.NO_CONTENT, controllerCar.getStatusCode());
    }


    @Test
    public void failToDeleteCar() throws EntityNotFoundException
    {

        Mockito.doThrow(new EntityNotFoundException("Failed to find the car")).when(service).delete(123L);
        ResponseEntity<?> controllerCar = controller.deleteCar(123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerCar.getStatusCode());
        Assert.assertTrue((controllerCar.getBody().toString()).contains("Failed to find the car"));
    }


    @Test
    public void updateCarSuccessfully() throws ConstraintsViolationException, EntityNotFoundException
    {
        car.setConvertible(true);
        Mockito.when(service.update(Matchers.anyLong(), Matchers.any(CarDO.class))).thenReturn(car);
        ResponseEntity<?> controllerCar = controller.updateCar(123L, carDTO);
        Assert.assertEquals(HttpStatus.OK, controllerCar.getStatusCode());
        Assert.assertEquals(carDTO.getId(), ((CarDO) controllerCar.getBody()).getId());
        Assert.assertEquals(true, ((CarDO) controllerCar.getBody()).getConvertible());
    }


    @Test
    public void failToUpdateCarDueToEntityNotFound() throws ConstraintsViolationException, EntityNotFoundException
    {
        car.setConvertible(true);
        Mockito.when(service.update(Matchers.anyLong(), Matchers.any(CarDO.class))).thenThrow(new EntityNotFoundException("Failed to find the car"));
        ResponseEntity<?> controllerCar = controller.updateCar(123L, carDTO);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerCar.getStatusCode());
        Assert.assertTrue(((String) controllerCar.getBody()).contains("Failed to find the car"));
    }


    @Test
    public void failToUpdateCarDueToDataIntegrity() throws ConstraintsViolationException, EntityNotFoundException
    {
        car.setConvertible(true);
        Mockito.when(service.update(Matchers.anyLong(), Matchers.any(CarDO.class))).thenThrow(new ConstraintsViolationException("Missing fields"));
        ResponseEntity<?> controllerCar = controller.updateCar(123L, carDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerCar.getStatusCode());
        Assert.assertTrue(((String) controllerCar.getBody()).contains("Missing fields"));
    }


    @Test
    public void rateCarSuccessfully() throws InvalidRatingValueException, EntityNotFoundException
    {
        Mockito.when(service.rateCar(123L, 5D)).thenReturn(car);
        ResponseEntity<?> controllerCar = controller.rateCar(123L, 5D);
        Assert.assertEquals(HttpStatus.OK, controllerCar.getStatusCode());
        Assert.assertEquals(car, controllerCar.getBody());
    }


    @Test
    public void failToRateCarDueToCarNotFound() throws InvalidRatingValueException, EntityNotFoundException
    {
        Mockito.when(service.rateCar(123L, 5D)).thenThrow(new EntityNotFoundException("Failed to find the car"));
        ResponseEntity<?> controllerCar = controller.rateCar(123L, 5D);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerCar.getStatusCode());
        Assert.assertTrue(controllerCar.getBody().toString().contains("Failed to find the car"));
    }


    @Test
    public void failToRateCarDueToInvalidRatingValueException() throws InvalidRatingValueException, EntityNotFoundException
    {
        Mockito.when(service.rateCar(123L, 5D)).thenThrow(new InvalidRatingValueException("The value must be between 0 and 5"));
        ResponseEntity<?> controllerCar = controller.rateCar(123L, 5D);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerCar.getStatusCode());
        Assert.assertTrue(controllerCar.getBody().toString().contains("The value must be between 0 and 5"));
    }


    @Test
    public void getAllCars()
    {
        Mockito.when(service.findAll()).thenReturn(Collections.singletonList(car));
        ResponseEntity<?> allCars = controller.getAllCars();
        Assert.assertEquals(HttpStatus.OK, allCars.getStatusCode());
        Assert.assertEquals(123L, ((List<CarDTO>) allCars.getBody()).get(0).getId(), 0);
    }
}
