package com.salaboy.cdf.services;

import com.salaboy.cdf.model.dao.CloudEventRepository;
import com.salaboy.cdf.model.entities.CloudEventEntity;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EventStoreService {


    @Autowired
    private CloudEventRepository cloudEventRepository;

    public String addEventToStore(CloudEvent cloudEvent) {
        byte[] serialized = EventFormatProvider
                .getInstance()
                .resolveFormat(JsonFormat.CONTENT_TYPE)
                .serialize(cloudEvent);

        CloudEventEntity cloudEventEntity = new CloudEventEntity();
        if(cloudEvent.getExtension("cdfprojectname") != null) {
            String projectName = cloudEvent.getExtension("cdfprojectname").toString();
            cloudEventEntity.setProjectName(projectName);
            if( cloudEvent.getExtension("cdfmodulename") != null){
                String moduleName = cloudEvent.getExtension("cdfmodulename").toString();
                cloudEventEntity.setModuleName(moduleName);
            }

        }else if(cloudEvent.getExtension("cdfenvname") != null){
            String envName = cloudEvent.getExtension("cdfenvname").toString();
            cloudEventEntity.setEnvironmentName(envName);
            if(cloudEvent.getExtension("cdfservicename") != null) {
                String serviceName = cloudEvent.getExtension("cdfservicename").toString();
                cloudEventEntity.setServiceName(serviceName);
            }
        }

        String cloudEventSerialized = new String(serialized);
        log.info("Serialized Cloud Event: " + cloudEventSerialized);

        cloudEventEntity.setCloudEvent(cloudEventSerialized);
        cloudEventEntity.setType(cloudEvent.getType());

        cloudEventRepository.save(cloudEventEntity);

        return serialized.toString();
    }

    public List<CloudEvent> getEventsForModule(String moduleName) {
        List<CloudEvent> ces = new ArrayList<>();
        Iterable<CloudEventEntity> byModuleName = cloudEventRepository.findByModuleName(moduleName);
        for (CloudEventEntity cee : byModuleName) {
            ces.add(EventFormatProvider
                    .getInstance()
                    .resolveFormat(JsonFormat.CONTENT_TYPE).deserialize(cee.getCloudEvent().getBytes()));
        }
        return ces;
    }

    public List<CloudEvent> getEventsForService(String serviceName) {
        List<CloudEvent> ces = new ArrayList<>();
        Iterable<CloudEventEntity> byServiceName = cloudEventRepository.findByServiceName(serviceName);
        for (CloudEventEntity cee : byServiceName) {
            ces.add(EventFormatProvider
                    .getInstance()
                    .resolveFormat(JsonFormat.CONTENT_TYPE).deserialize(cee.getCloudEvent().getBytes()));
        }
        return ces;
    }

}
