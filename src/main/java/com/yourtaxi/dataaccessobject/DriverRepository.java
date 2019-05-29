package com.yourtaxi.dataaccessobject;

import com.yourtaxi.domainobject.DriverDO;
import com.yourtaxi.domainvalue.OnlineStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO>
{

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);

    List<DriverDO> findByCarId(Long carId);

    DriverDO findFirstByOrderByIdDesc();
}
