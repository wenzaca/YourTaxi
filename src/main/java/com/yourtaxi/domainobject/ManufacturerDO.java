package com.yourtaxi.domainobject;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name = "manufacturer"
)
public class ManufacturerDO
{

    @Id
    @Column(nullable = false)
    @NotNull(message = "Manufacturer name can not be null!")
    @NotEmpty(message = "Manufacturer name can not be empty!")
    private String name;


    private ManufacturerDO()
    {
    }


    public ManufacturerDO(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }

}
