package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.EnvironmentId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface EnvironmentRepository extends PagingAndSortingRepository<Environment, EnvironmentId> {
    @Query("SELECT e FROM Environment e WHERE e.name = ?1")
    List<Environment> findByName(String name);

}
