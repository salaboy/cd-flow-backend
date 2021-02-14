package com.salaboy.cdf.model.dao;

import com.salaboy.cdf.model.entities.CloudEventEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CloudEventRepository extends PagingAndSortingRepository<CloudEventEntity, Long> {

    Iterable<CloudEventEntity> findByModuleName(String moduleName);

    Iterable<CloudEventEntity> findByProjectName(String projectName);

    Iterable<CloudEventEntity> findByEnvironmentName(String environmentName);

    Iterable<CloudEventEntity> findByServiceName(String serviceName);
}


