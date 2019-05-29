package com.yourtaxi.controller.mapper;

import com.yourtaxi.datatransferobject.CarDTO;
import com.yourtaxi.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO carDTO)
    {
        return new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount(), carDTO.getConvertible(), carDTO.getEngineType(), carDTO.getManufacturer());
    }


    public static CarDTO makeCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
            .setId(carDO.getId())
            .setLicensePlate(carDO.getLicensePlate())
            .setSeatCount(carDO.getSeatCount())
            .setConvertible(carDO.getConvertible())
            .setEngineType(carDO.getEngineType())
            .setManufacturer(carDO.getManufacturer());

        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
