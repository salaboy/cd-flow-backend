package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.ArtifactEvent;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArtifactEventsRepository extends PagingAndSortingRepository<ArtifactEvent, Long> {


}
