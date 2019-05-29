package com.yourtaxi.controller;

import com.yourtaxi.controller.filter.ChainFilter;
import com.yourtaxi.controller.filter.criteria.DriverCriteriaSearchBuilder;
import com.yourtaxi.controller.mapper.DriverMapper;
import com.yourtaxi.datatransferobject.DriverDTO;
import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.OnlineStatus;
import com.yourtaxi.exception.CarAlreadyInUseException;
import com.yourtaxi.exception.ConstraintsViolationException;
import com.yourtaxi.exception.DriverOfflinePickCarException;
import com.yourtaxi.exception.EntityNotFoundException;
import com.yourtaxi.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public ResponseEntity<?> getDriver(@Valid @PathVariable long driverId)
    {
        try
        {
            return new ResponseEntity<>(DriverMapper.makeDriverDTO(driverService.find(driverId)), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createDriver(@Valid @RequestBody DriverDTO driverDTO)
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        try
        {
            return new ResponseEntity<>(DriverMapper.makeDriverDTO(driverService.create(driverDO)), HttpStatus.CREATED);
        }
        catch (ConstraintsViolationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{driverId}")
    public ResponseEntity<?> deleteDriver(@Valid @PathVariable long driverId)
    {
        try
        {
            driverService.delete(driverId);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{driverId}")
    public ResponseEntity<?> updateLocation(
        @Valid @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
    {
        try
        {
            driverService.updateLocation(driverId, longitude, latitude);
            return ResponseEntity.ok().build();
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/{driverId}/car")
    public ResponseEntity<?> updateCarSelection(
        @Valid @PathVariable long driverId, @RequestParam(required = false) Long carId)
    {
        try
        {
            return new ResponseEntity<>(DriverMapper.makeDriverDTO(driverService.selectCar(driverId, carId)), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (CarAlreadyInUseException | DriverOfflinePickCarException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/{driverId}")
    public ResponseEntity<?> updateDriverStatus(
        @Valid @PathVariable long driverId)
    {
        try
        {
            return new ResponseEntity<>(DriverMapper.makeDriverDTO(driverService.updateDriverStatus(driverId)), HttpStatus.OK);
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<?> search(
        @RequestParam(value = "search") String search)
    {
        ChainFilter chain = new ChainFilter();
        chain.addFilters(getDriverCriteriaSearchBuilder(search).build());
        List<DriverDTO> drivers;
        try
        {
            drivers = DriverMapper.makeDriverDTOList(chain.filter(driverService.findAll()));
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (drivers.isEmpty())
        {
            ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return new ResponseEntity<>(DriverMapper.makeDriverDTOList(driverService.find(onlineStatus)), HttpStatus.OK);
    }


    private DriverCriteriaSearchBuilder getDriverCriteriaSearchBuilder(String search)
    {
        DriverCriteriaSearchBuilder driverCriteriaSearchBuilder = new DriverCriteriaSearchBuilder();
        if (search != null)
        {
            Pattern pattern = Pattern.compile("(.+?)(:|<|>)(.+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find())
            {
                driverCriteriaSearchBuilder.with(matcher.group(1),
                    matcher.group(2), matcher.group(3));
            }
        }
        return driverCriteriaSearchBuilder;
    }
}
