package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.EnvironmentId;
import com.salaboy.cdf.model.entities.run.Service;
import com.salaboy.cdf.model.entities.run.ServiceId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface ServiceRepository extends PagingAndSortingRepository<Service, ServiceId> {
    @Query("SELECT s FROM Service s WHERE s.name = ?2 AND s.environment.name = ?1")
    Optional<Service> findByName(String envName, String serviceName);

}
