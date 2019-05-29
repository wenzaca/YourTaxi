package com.yourtaxi.dataaccessobject;

import com.yourtaxi.domainobject.ManufacturerDO;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface ManufacturerRepository extends CrudRepository<ManufacturerDO, String>
{

}
