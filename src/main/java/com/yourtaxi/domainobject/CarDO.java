package com.yourtaxi.domainobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yourtaxi.domainvalue.EngineType;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name = "car"
)
public class CarDO
{

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull(message = "The license plate can not be null!")
    @Column(nullable = false, name = "LICENSE_PLATE")
    private String licensePlate;

    @NotNull(message = "The number of seats must be specified!")
    @Column(nullable = false, name = "SEAT_COUNT")
    @Min(value = 2L, message = "The minimum number of seats is 2")
    @Max(value = 9L, message = "The maximum number of seats is 5")
    private Integer seatCount;

    @Column(nullable = false)
    private Boolean convertible;

    @Column
    private Boolean deleted;

    @Min(value = 0, message = "The minimum rate is 0")
    @Max(value = 5L, message = "The maximum rate is 5")
    @Column
    private Double rating = 0D;

    @Column
    @JsonIgnore
    private Long numberOfRatings = 0L;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The type of the Engine must be specified")
    @Column(name = "ENGINE_TYPE")
    private EngineType engineType;

    @ManyToOne
    @NotNull(message = "The Manufacturer must be specified")
    private ManufacturerDO manufacturer;


    private CarDO()
    {
    }


    public CarDO(String licensePlate, Integer seatCount, Boolean convertible, EngineType engineType, ManufacturerDO manufacturer)
    {

        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
        this.rating = 0D;
        this.numberOfRatings = 0L;
        this.deleted = false;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }


    public ManufacturerDO getManufacturer()
    {
        return manufacturer;
    }


    public void setManufacturer(ManufacturerDO manufacturer)
    {
        this.manufacturer = manufacturer;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public Boolean getDeleted()
    {
        return deleted;
    }


    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }


    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public void setConvertible(Boolean convertible)
    {
        this.convertible = convertible;
    }


    public Double getRating()
    {
        return rating;
    }


    public void setRating(Double rating)
    {
        this.rating = rating;
    }


    public Long getNumberOfRatings()
    {
        return numberOfRatings;
    }


    public void setNumberOfRatings(Long numberOfRatings)
    {
        this.numberOfRatings = numberOfRatings;
    }


    public EngineType getEngineType()
    {
        return engineType;
    }


    public void setEngineType(EngineType engineType)
    {
        this.engineType = engineType;
    }
}
