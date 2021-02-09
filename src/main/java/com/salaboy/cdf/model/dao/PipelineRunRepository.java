package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.model.entities.build.PipelineRunId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PipelineRunRepository extends PagingAndSortingRepository<PipelineRun, PipelineRunId> {

    @Query("SELECT pr FROM PipelineRun pr WHERE pr.module.project.name = ?1 AND pr.module.name = ?2 AND pr.pipelineId = ?3")
    Optional<PipelineRun> findByPipelineId(String projectName, String moduleName, String id);


}
