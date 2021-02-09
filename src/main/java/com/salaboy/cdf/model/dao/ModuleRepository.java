package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.ModuleId;
import com.salaboy.cdf.model.entities.build.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends PagingAndSortingRepository<Module, ModuleId> {


    @Query("SELECT m FROM Module m WHERE m.name = ?2 AND m.project.name = ?1")
    Optional<Module> findByName(String projectName, String moduleName);
}
