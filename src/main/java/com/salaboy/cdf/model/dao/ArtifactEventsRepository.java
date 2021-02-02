package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.ArtifactEvent;
import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtifactEventsRepository extends PagingAndSortingRepository<ArtifactEvent, Long> {


}
