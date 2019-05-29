package com.yourtaxi.dataaccessobject;

import com.yourtaxi.domainobject.CarDO;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface CarRepository extends CrudRepository<CarDO, Long>
{
    CarDO findFirstByOrderByIdDesc();
}
