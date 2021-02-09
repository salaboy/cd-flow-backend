package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.model.entities.build.ProjectId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ProjectRepository extends PagingAndSortingRepository<Project, ProjectId> {
    @Query("SELECT p FROM Project p WHERE p.name = ?1")
    List<Project> findByName(String name);

}
