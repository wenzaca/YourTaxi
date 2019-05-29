package com.yourtaxi.controller;

import com.yourtaxi.controller.mapper.DriverMapper;
import com.yourtaxi.datatransferobject.DriverDTO;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainobject.ManufacturerDO;
import com.yourtaxi.domainvalue.EngineType;
import com.yourtaxi.domainvalue.GeoCoordinate;
import com.yourtaxi.domainvalue.OnlineStatus;
import com.yourtaxi.exception.CarAlreadyInUseException;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.DriverOfflinePickCarException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.service.driver.impl.DefaultDriverService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DriverControllerTest
{

    @Mock
    private DefaultDriverService service;

    @InjectMocks
    private DriverController controller;

    private DriverDO driver;

    private DriverDO driver2;

    private DriverDTO driverDTO;

    private CarDO car;

    private CarDO car2;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        driver = new DriverDO("admin", "yourTaxi");
        driver.setId(123L);
        driver.setCoordinate(new GeoCoordinate(10, 10));
        driver2 = new DriverDO("Luke", "yourTaxi");
        driver2.setId(456L);
        driver2.setCoordinate(new GeoCoordinate(-10, 80));
        driverDTO = DriverMapper.makeDriverDTO(driver);
        car = new CarDO("123LY", 2, false, EngineType.DIESEL, new ManufacturerDO("BMW"));
        car.setId(123L);
        car2 = new CarDO("456PT", 4, true, EngineType.GAS, new ManufacturerDO("Mercedes"));
        car2.setId(456L);
    }


    @Test
    public void getDriverSuccessfully() throws EntityNotFoundException
    {

        Mockito.when(service.find(123L))
            .thenReturn(driver);
        ResponseEntity<?> controllerDriver = controller.getDriver(123L);
        Assert.assertEquals(HttpStatus.OK, controllerDriver.getStatusCode());
        Assert.assertEquals(driverDTO.getId(), ((DriverDTO) controllerDriver.getBody()).getId());
    }


    @Test
    public void failToGetDriver() throws EntityNotFoundException
    {

        Mockito.when(service.find(123L))
            .thenThrow(new EntityNotFoundException("Failed to find the driver"));
        ResponseEntity<?> controllerDriver = controller.getDriver(123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue(((String) controllerDriver.getBody()).contains("Failed to find the driver"));
    }


    @Test
    public void createDriverSuccessFully() throws ConstraintsViolationException
    {

        Mockito.when(service.create(Matchers.any(DriverDO.class)))
            .thenReturn(driver);
        ResponseEntity<?> controllerDriver = controller.createDriver(driverDTO);
        Assert.assertEquals(HttpStatus.CREATED, controllerDriver.getStatusCode());
        Assert.assertEquals(driverDTO.getId(), ((DriverDTO) controllerDriver.getBody()).getId());
    }


    @Test
    public void failToCreateDriver() throws ConstraintsViolationException
    {

        Mockito.when(service.create(Matchers.any(DriverDO.class)))
            .thenThrow(new ConstraintsViolationException("Missing fields"));
        ResponseEntity<?> controllerDriver = controller.createDriver(driverDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerDriver.getStatusCode());
        Assert.assertTrue(((String) controllerDriver.getBody()).contains("Missing fields"));
    }


    @Test
    public void deleteDriverSuccessfully() throws EntityNotFoundException
    {

        Mockito.doNothing()
            .when(service)
            .delete(123L);
        ResponseEntity<?> controllerDriver = controller.deleteDriver(123L);
        Assert.assertEquals(HttpStatus.NO_CONTENT, controllerDriver.getStatusCode());
    }


    @Test
    public void failToDeleteDriver() throws EntityNotFoundException
    {

        Mockito.doThrow(new EntityNotFoundException("Failed to find the driver"))
            .when(service)
            .delete(123L);
        ResponseEntity<?> controllerDriver = controller.deleteDriver(123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue((controllerDriver.getBody()
            .toString()).contains("Failed to find the driver"));
    }


    @Test
    public void updateDriverLocationSuccessfully() throws EntityNotFoundException
    {

        Mockito.doNothing()
            .when(service)
            .updateLocation(123L, 10, 10);
        ResponseEntity<?> controllerDriver = controller.updateLocation(123L, 10, 10);
        Assert.assertEquals(HttpStatus.OK, controllerDriver.getStatusCode());
    }


    @Test
    public void failToUpdateDriverLocationDueToEntityNotFound() throws EntityNotFoundException
    {

        Mockito.doThrow(new EntityNotFoundException("Failed to find the driver"))
            .when(service)
            .updateLocation(123L, 10, 10);
        ResponseEntity<?> controllerDriver = controller.updateLocation(123L, 10, 10);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("Failed to find the driver"));

    }


    @Test
    public void selectCarSuccessfully()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        driver.setCar(car);
        Mockito.when(service.selectCar(123L, 123L))
            .thenReturn(driver);
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, 123L);
        Assert.assertEquals(HttpStatus.OK, controllerDriver.getStatusCode());
        Assert.assertEquals(driver.getId(), ((DriverDTO) controllerDriver.getBody()).getId());
    }


    @Test
    public void failToSelectCarDueToCarOrDriverNotFound()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        driver.setCar(car);
        Mockito.when(service.selectCar(123L, 123L))
            .thenThrow(new EntityNotFoundException("Failed to find the driver"));
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, 123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("Failed to find the driver"));
    }


    @Test
    public void failToSelectCarDueToCarOrDriverNotFoundAlreadyInUse()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        driver.setCar(car);
        Mockito.when(service.selectCar(123L, 123L))
            .thenThrow(new CarAlreadyInUseException("This car is in use by another driver"));
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, 123L);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("This car is in use by another driver"));
    }


    @Test
    public void failToSelectCarDueToCarOrDriverIsOffline()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        driver.setOnlineStatus(OnlineStatus.OFFLINE);
        driver.setCar(car);
        Mockito.when(service.selectCar(123L, 123L))
            .thenThrow(new DriverOfflinePickCarException("This driver must be online to pick a car"));
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, 123L);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("This driver must be online to pick a car"));
    }


    @Test
    public void deselectCarSuccessfully()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        Mockito.when(service.selectCar(123L, null))
            .thenReturn(driver);
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, null);
        Assert.assertEquals(HttpStatus.OK, controllerDriver.getStatusCode());
        Assert.assertEquals(123L, ((DriverDTO) controllerDriver.getBody()).getId(), 0);
    }


    @Test
    public void failToDeselectCarDueToEntityNotFound()
        throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {

        Mockito.when(service.selectCar(123L, null))
            .thenThrow(new EntityNotFoundException("Failed to find the driver"));
        ResponseEntity<?> controllerDriver = controller.updateCarSelection(123L, null);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("Failed to find the driver"));
    }


    @Test
    public void updateDriverStatusSuccessfully() throws EntityNotFoundException
    {

        driver.setOnlineStatus(OnlineStatus.ONLINE);
        Mockito.when(service.updateDriverStatus(123L))
            .thenReturn(driver);
        ResponseEntity<?> controllerDriver = controller.updateDriverStatus(123L);
        Assert.assertEquals(HttpStatus.OK, controllerDriver.getStatusCode());
        Assert.assertEquals(123L, ((DriverDTO) controllerDriver.getBody()).getId(), 0);
    }


    @Test
    public void failToUpdateDriverStatusDueToEntityNotFound() throws EntityNotFoundException
    {

        Mockito.when(service.updateDriverStatus(123L))
            .thenThrow(new EntityNotFoundException("Failed to find the driver"));
        ResponseEntity<?> controllerDriver = controller.updateDriverStatus(123L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, controllerDriver.getStatusCode());
        Assert.assertTrue(controllerDriver.getBody()
            .toString()
            .contains("Failed to find the driver"));
    }


    @Test
    public void getAllDrivers()
    {

        driver.setOnlineStatus(OnlineStatus.ONLINE);
        Mockito.when(service.find(OnlineStatus.ONLINE))
            .thenReturn(Collections.singletonList(driver));
        ResponseEntity<?> allDrivers = controller.findDrivers(OnlineStatus.ONLINE);
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(123L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByStatusAndDriverUsernameAndCoordinate()
    {

        driver.setOnlineStatus(OnlineStatus.ONLINE);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("onlineStatus:ONLINE,username:admin,coordinate:10^10");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(123L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByCarIdAndCarManufacturer()
    {

        driver.setCar(car);
        driver2.setCar(car2);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("car.id:123,car.manufacturer:BMW");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(123L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByCarEngineTypeAndCarConvertible()
    {

        driver.setCar(car);
        driver2.setCar(car2);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("car.convertible:true,car.engineType:GAS");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(456L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByCarRatingAndCarSeatCountGreaterThan()
    {

        car.setRating(4D);
        car.setNumberOfRatings(10L);
        car2.setRating(3D);
        car2.setNumberOfRatings(3L);
        driver.setCar(car);
        driver2.setCar(car2);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("car.rating>3,car.seatCount>1");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(123L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByCarRatingAndCarSeatCountSmallerThan()
    {

        car.setRating(4D);
        car.setNumberOfRatings(10L);
        car2.setRating(3D);
        car2.setNumberOfRatings(3L);
        driver.setCar(car);
        driver2.setCar(car2);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("car.rating<4,car.seatCount<5");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(456L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }


    @Test
    public void searchByCarRatingAndCarSeatCountEquals()
    {

        car.setRating(4D);
        car.setNumberOfRatings(10L);
        car2.setRating(3D);
        car2.setNumberOfRatings(3L);
        driver.setCar(car);
        driver2.setCar(car2);
        Mockito.when(service.findAll())
            .thenReturn(Arrays.asList(driver, driver2));
        ResponseEntity<?> allDrivers = controller.search("car.rating:4,car.seatCount:2");
        Assert.assertEquals(HttpStatus.OK, allDrivers.getStatusCode());
        Assert.assertEquals(1, ((List<DriverDTO>) allDrivers.getBody()).size());
        Assert.assertEquals(123L, ((List<DriverDTO>) allDrivers.getBody()).get(0)
            .getId(), 0);
    }
}
