package com.yourtaxi.controller;

import com.yourtaxi.dataaccessobject.CarRepository;
import com.yourtaxi.domainobject.CarDO;
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
public class CarControllerIntegrationTest
{

    private static Long carId = 0L;
    private final String URL = "/v1/car/";
    private final String CAR = this.getJsonFromResources();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;


    @Before
    public void setUp()
    {

        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
            .build();
        if (carId == 0L)
        {
            CarDO oldId = carRepository.findFirstByOrderByIdDesc();
            carId = oldId.getId() + 1;
            carRepository.delete(oldId);
        }
    }


    @After
    public void tearDown()
    {
        CarDO oldId = carRepository.findFirstByOrderByIdDesc();
        carId = oldId.getId() + 1;
        carRepository.delete(oldId);
    }


    @Test
    public void getCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(get(URL
            + carId.toString()));

    }


    @Test
    public void failToGetCar() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(get(URL + carId.toString() + "1"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToCreateCar() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR.replace("2", "0")))
            .andExpect(status().isBadRequest());
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
    }


    @Test
    public void deleteCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(delete(URL + carId.toString()))
            .andExpect(status().isNoContent());

    }


    @Test
    public void failToDeleteCar() throws Exception
    {

        mvc.perform(delete(URL + carId.toString()))
            .andExpect(status().isNotFound());
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
    }


    @Test
    public void updateCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + carId.toString()).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isOk());
    }


    @Test
    public void failToUpdateCarDueToEntityNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + carId.toString() + 1).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToUpdateCarDueToDataIntegrity() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(put(URL + carId.toString() + 1).contentType(MediaType.APPLICATION_JSON)
            .content(CAR.replace("2", "0")))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void rateCarSuccessfully() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + carId.toString() + "/rate").param("carRate", "5"))
            .andExpect(status().isOk());
    }


    @Test
    public void rateCarSuccessfullyWithDecimalValue() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + carId.toString() + "/rate").param("carRate", "4.3"))
            .andExpect(status().isOk());
    }


    @Test
    public void failToRateCarDueToCarNotFound() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + carId.toString() + "1/rate").param("carRate", "5"))
            .andExpect(status().isNotFound());
    }


    @Test
    public void failToRateCarDueToInvalidRatingValueException() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(patch(URL + carId.toString() + "/rate").param("carRate", "8"))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void getAllCars() throws Exception
    {

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(CAR))
            .andExpect(status().isCreated());
        mvc.perform(get(URL))
            .andExpect(status().isOk());
    }


    private String getJsonFromResources()
    {

        String result;

        ClassLoader classLoader = getClass().getClassLoader();

        InputStream vendorJsonAsStream = classLoader.getResourceAsStream("car-dto.json");
        result = new BufferedReader(new InputStreamReader(vendorJsonAsStream)).lines()
            .collect(Collectors.joining("\n"));

        return result;
    }
}
