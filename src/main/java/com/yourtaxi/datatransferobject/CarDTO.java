package com.yourtaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yourtaxi.domainobject.ManufacturerDO;
import com.yourtaxi.domainvalue.EngineType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO
{
    @JsonIgnore
    private Long id;

    @NotNull(message = "The license plate can not be null!")
    private String licensePlate;

    @NotNull(message = "The number of seats must be specified!")
    @Min(value = 2L, message = "The minimum number of seats is 2")
    @Max(value = 9L, message = "The maximum number of seats is 5")
    private Integer seatCount;

    private Boolean convertible = false;

    @NotNull(message = "The type of the Engine must be specified")
    private EngineType engineType;

    @NotNull(message = "The Manufacturer must be specified")
    private ManufacturerDO manufacturer;


    private CarDTO()
    {
    }


    private CarDTO(Long id, String licensePlate, Integer seatCount, Boolean convertible, EngineType engineType, ManufacturerDO manufacturer)
    {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }


    public static CarDTOBuilder newBuilder()
    {
        return new CarDTOBuilder();
    }


    @JsonProperty
    public Long getId()
    {
        return id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public EngineType getEngineType()
    {
        return engineType;
    }


    public ManufacturerDO getManufacturer()
    {
        return manufacturer;
    }


    public static class CarDTOBuilder
    {
        private Long id;
        private String licensePlate;
        private Integer seatCount;
        private Boolean convertible;
        private EngineType engineType;
        private ManufacturerDO manufacturer;


        public CarDTOBuilder setId(Long id)
        {
            this.id = id;
            return this;
        }


        public CarDTOBuilder setLicensePlate(String licensePlate)
        {
            this.licensePlate = licensePlate;
            return this;
        }


        public CarDTOBuilder setSeatCount(Integer seatCount)
        {
            this.seatCount = seatCount;
            return this;
        }


        public CarDTOBuilder setConvertible(Boolean convertible)
        {
            this.convertible = convertible;
            return this;
        }


        public CarDTOBuilder setEngineType(EngineType engineType)
        {
            this.engineType = engineType;
            return this;
        }


        public CarDTOBuilder setManufacturer(ManufacturerDO manufacturer)
        {
            this.manufacturer = manufacturer;
            return this;
        }


        public CarDTO createCarDTO()
        {
            return new CarDTO(id, licensePlate, seatCount, convertible, engineType, manufacturer);
        }

    }
}
