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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class EventStoreService {


    @Autowired
    private CloudEventRepository cloudEventRepository;

    public String addEventToModule(CloudEvent cloudEvent) {
        byte[] serialized = EventFormatProvider
                .getInstance()
                .resolveFormat(JsonFormat.CONTENT_TYPE)
                .serialize(cloudEvent);

        if (cloudEvent.getType().equals("CDF.Project.Created")) { // ignore for now
            return serialized.toString();
        }

        String moduleName = cloudEvent.getExtension("cdfmodulename").toString();


        String cloudEventSerialized = new String(serialized);
        log.info("Serialized Cloud Event: " + cloudEventSerialized);

        CloudEventEntity cloudEventEntity = new CloudEventEntity();
        cloudEventEntity.setCloudEvent(cloudEventSerialized);
        cloudEventEntity.setModuleName(moduleName);
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

}
