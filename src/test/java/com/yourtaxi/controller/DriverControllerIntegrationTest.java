package com.yourtaxi.controller;

import com.yourtaxi.dataaccessobject.DriverRepository;
import com.yourtaxi.domainobject.DriverDO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DriverControllerIntegrationTest
{

    private static Long driverId = 0L;
    private final String URL = "/v1/drivers/";
    private final String DRIVER = this.getJsonFromResources();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
            .build();
        if (driverId == 0L)
        {
            DriverDO oldDriverId = driverRepository.findFirstByOrderByIdDesc();
            driverId = oldDriverId.getId() + 1;
        }

    }


    @After
    public void tearDown()
    {

        DriverDO oldDriverId = driverRepository.findFirstByOrderByIdDesc();
        driverRepository.delete(oldDriverId);
        driverId = oldDriverId.getId() + 1;
    }


    @Test
    public void getDriverSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL
            + driverId.toString()));

    }


    @Test
    public void failToGetDriver() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + driverId.toString() + "1"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToCreateDriver() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER.replace("\"password\": \"admin\",", "")))
            .andExpect(status().isBadRequest());
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
    }


    @Test
    public void deleteDriverSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(delete(URL + driverId.toString()))
            .andExpect(status().isNoContent());

    }


    @Test
    public void failToDeleteDriver() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(delete(URL + driverId.toString() + 1))
            .andExpect(status().isNotFound());
    }


    @Test
    public void updateDriverLocationSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + driverId.toString()).param("latitude", "10")
            .param("longitude", "10"))
            .andExpect(status().isOk());
    }


    @Test
    public void failToUpdateDriverLocationDueToInvalidLatitudeValue() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + driverId.toString()).param("latitude", "1000")
            .param("longitude", "10"))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void failToUpdateDriverLocationDueToEntityNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + driverId.toString() + 1).param("latitude", "10")
            .param("longitude", "10"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void selectCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString()))
            .andExpect(status().isOk());
        mvc.perform(patch(URL + driverId.toString() + "/car").param("carId", "2"))
            .andExpect(status().isOk());
    }


    @Test
    public void failToSelectCarDueToCarOrDriverNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString() + 1 + "/car").param("carId", "3"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToSelectCarDueToCarAlreadyInUse() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString()))
            .andExpect(status().isOk());
        mvc.perform(patch(URL + driverId.toString() + "/car").param("carId", "2"))
            .andExpect(status().isOk());
        mvc.perform(patch(URL + driverId.toString() + "/car").param("carId", "2"))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void deselectCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString() + "/car"))
            .andExpect(status().isOk());
    }


    @Test
    public void failToDeselectCarDueToEntityNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString() + 1 + "/car"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void updateDriverStatusSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString()))
            .andExpect(status().isOk());
    }


    @Test
    public void failToUpdateDriverStatusDueToEntityNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + driverId.toString() + 1))
            .andExpect(status().isNotFound());
    }


    @Test
    public void getAllDrivers() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL).param("onlineStatus", "ONLINE"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByStatusAndDriverUsernameAndCoordinate() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter?search=onlineStatus:ONLINE,username:admin,coordinate:10^10"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByCarIdAndCarManufacturer() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter?search=car.id:123,car.manufacturer:BMW"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByCarEngineTypeAndCarConvertible() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter?search=car.convertible:true,car.engineType:GAS"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByCarRatingAndCarSeatCountGreaterThan() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter?search=car.rating>3,car.seatCount>1"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByCarRatingAndCarSeatCountSmallerThan() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter").param("search", "car.rating<4,car.seatCount<5"))
            .andExpect(status().isOk());
    }


    @Test
    public void searchByCarRatingAndCarSeatCountEquals() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(DRIVER))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + "/filter").param("search", "car.rating:4,car.seatCount:2"))
            .andExpect(status().isOk());
    }


    private String getJsonFromResources()
    {

        String result;

        ClassLoader classLoader = getClass().getClassLoader();

        InputStream vendorJsonAsStream = classLoader.getResourceAsStream("driver-dto.json");
        result = new BufferedReader(new InputStreamReader(vendorJsonAsStream)).lines()
            .collect(Collectors.joining("\n"));

        return result;
    }
}
