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

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDriverServiceTest
{

    @Mock
    private DriverRepository repo;

    @Mock
    private CarService carService;

    @InjectMocks
    private DefaultDriverService driverService;

    private DriverDO driver;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        driver = new DriverDO("admin", "admin");
        driver.setId(123L);
        driver.setOnlineStatus(OnlineStatus.ONLINE);
    }


    @Test
    public void findDriverWhenGettingItById() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        Assert.assertEquals(driver, driverService.find(123L));
    }


    @Test(expected = EntityNotFoundException.class)
    public void getExceptionWhenTryToFindDriverByIdThatDoesNotExist() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(null);

        driverService.find(123L);
    }


    @Test
    public void createNewDriverShouldReturnIt() throws ConstraintsViolationException
    {
        Mockito.when(repo.save(driver)).thenReturn(driver);

        Assert.assertEquals(driver, driverService.create(driver));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void failToCreateNewDriverShouldReturnDataIntegrityViolationException() throws ConstraintsViolationException
    {
        Mockito.when(repo.save(driver)).thenThrow(new DataIntegrityViolationException("Username and password can not be null"));

        driverService.create(driver);
    }


    @Test
    public void delete() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        driverService.delete(123L);

        Mockito.verify(repo, Mockito.atLeastOnce()).findOne(123L);
        Assert.assertEquals(null, driver.getCar());
        Assert.assertEquals(true, driver.getDeleted());
    }


    @Test
    public void updateLocationWithCorrectParam() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        driverService.updateLocation(123L, 10, 10);

        Mockito.verify(repo, Mockito.atLeastOnce()).findOne(123L);
        Assert.assertEquals(new GeoCoordinate(10, 10), driver.getCoordinate());
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateLocationWithWrongLatitude() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        driverService.updateLocation(123L, 10, -100);

        Mockito.verify(repo, Mockito.atLeastOnce()).findOne(123L);
    }


    @Test
    public void findByOnlineStatus()
    {
        Mockito.when(repo.findByOnlineStatus(OnlineStatus.ONLINE)).thenReturn(Collections.singletonList(driver));

        Assert.assertEquals(Collections.singletonList(driver), driverService.find(OnlineStatus.ONLINE));
    }


    @Test
    public void selectCarSuccessfully() throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {
        CarDO car = Mockito.mock(CarDO.class);
        Mockito.when(carService.find(431L)).thenReturn(car);
        Mockito.when(repo.findByCarId(431L)).thenReturn(Collections.emptyList());
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        DriverDO driverDO = driverService.selectCar(123L, 431L);
        Assert.assertEquals(car, driverDO.getCar());
    }


    @Test(expected = CarAlreadyInUseException.class)
    public void tryToSelectCarThatIsAlreadyInUse() throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {
        CarDO car = Mockito.mock(CarDO.class);
        Mockito.when(carService.find(431L)).thenReturn(car);
        Mockito.when(repo.findByCarId(431L)).thenReturn(Collections.singletonList(Mockito.mock(DriverDO.class)));

        driverService.selectCar(123L, 431L);
    }


    @Test(expected = EntityNotFoundException.class)
    public void tryToSelectCarThatDoesNotExist() throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {
        Mockito.when(carService.find(431L)).thenThrow(new EntityNotFoundException("Car not found"));

        driverService.selectCar(123L, 431L);
    }


    @Test(expected = DriverOfflinePickCarException.class)
    public void tryToSelectCarWhileDriverIsOffline() throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {
        driver.setOnlineStatus(OnlineStatus.OFFLINE);
        CarDO car = Mockito.mock(CarDO.class);
        Mockito.when(carService.find(431L)).thenReturn(car);
        Mockito.when(repo.findByCarId(431L)).thenReturn(Collections.emptyList());
        Mockito.when(repo.findOne(123L)).thenReturn(driver);

        driverService.selectCar(123L, 431L);
    }


    @Test
    public void deselectCar() throws EntityNotFoundException, CarAlreadyInUseException, DriverOfflinePickCarException
    {
        driver.setCar(Mockito.mock(CarDO.class));
        Mockito.when(repo.findOne(123L)).thenReturn(driver);
        Assert.assertEquals(null, driverService.selectCar(123L, null).getCar());
    }


    @Test
    public void updateDriverStatusToOffline() throws EntityNotFoundException
    {
        Mockito.when(repo.findOne(123L)).thenReturn(driver);
        Assert.assertEquals(OnlineStatus.OFFLINE, driverService.updateDriverStatus(123L).getOnlineStatus());
    }


    @Test
    public void updateDriverStatusToOnline() throws EntityNotFoundException
    {
        driver.setOnlineStatus(OnlineStatus.OFFLINE);
        Mockito.when(repo.findOne(123L)).thenReturn(driver);
        Assert.assertEquals(OnlineStatus.ONLINE, driverService.updateDriverStatus(123L).getOnlineStatus());
    }


    @Test
    public void findAll()
    {
        Mockito.when(repo.findAll()).thenReturn(Collections.singletonList(driver));
        Assert.assertEquals(Collections.singletonList(driver), driverService.findAll());
    }
}
