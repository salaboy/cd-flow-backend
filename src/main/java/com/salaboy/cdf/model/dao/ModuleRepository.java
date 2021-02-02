package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {

    @Query("SELECT m FROM Module m WHERE m.project = ?1 AND m.name = ?2")
    Optional<Module> getModuleFromProject(Project project, String name);

    Optional<Module> findByName(String moduleName);
}
