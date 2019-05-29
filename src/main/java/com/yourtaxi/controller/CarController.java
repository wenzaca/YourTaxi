package com.yourtaxi.controller;

import com.yourtaxi.controller.mapper.CarMapper;
import com.yourtaxi.datatransferobject.CarDTO;
import com.yourtaxi.domainobject.CarDO;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.exception.InvalidRatingValueException;
import com.yourtaxi.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/car")
public class CarController
{

    private CarService carService;


    @Autowired
    public CarController(final CarService carService)
    {

        this.carService = carService;
    }


    @GetMapping("/{carId}")
    public ResponseEntity<?> getCar(@Valid @PathVariable long carId)
    {

        try
        {
            return new ResponseEntity<>(CarMapper.makeCarDTO(carService.find(carId)), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCar(@Valid @RequestBody CarDTO carDTO)
    {

        CarDO carDO = CarMapper.makeCarDO(carDTO);
        try
        {
            return new ResponseEntity<>(CarMapper.makeCarDTO(carService.create(carDO)), HttpStatus.CREATED);
        }
        catch (ConstraintsViolationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{carId}")
    public ResponseEntity<?> deleteCar(@Valid @PathVariable long carId)
    {

        try
        {
            carService.delete(carId);
            return ResponseEntity.noContent()
                .build();
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{carId}")
    public ResponseEntity<?> updateCar(@Valid @PathVariable long carId, @Valid @RequestBody CarDTO carDTO)
    {

        CarDO carDO = CarMapper.makeCarDO(carDTO);
        try
        {
            return new ResponseEntity<>(carService.update(carId, carDO), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ConstraintsViolationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/{carId}/rate")
    public ResponseEntity<?> rateCar(@Valid @PathVariable Long carId, @Valid @RequestParam Double carRate)
    {

        try
        {
            return new ResponseEntity<>(carService.rateCar(carId, carRate), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (InvalidRatingValueException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllCars()
    {

        return ResponseEntity.ok(CarMapper.makeCarDTOList(carService.findAll()));
    }
}
