package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.entities.PipelineRun;
import com.salaboy.cdf.model.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PipelineRunRepository extends PagingAndSortingRepository<PipelineRun, Long> {

    @Query("SELECT pr FROM PipelineRun pr WHERE pr.module.name = ?1 AND pr.pipelineId = ?2")
    Optional<PipelineRun> findByModuleAndId(String moduleName, String id);

    Optional<PipelineRun> findByPipelineId(String pipelineId);
}
