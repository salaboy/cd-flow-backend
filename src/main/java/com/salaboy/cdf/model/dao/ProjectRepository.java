package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.model.entities.build.ProjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, ProjectId> {
    Optional<Project> findByName(String name);

}
