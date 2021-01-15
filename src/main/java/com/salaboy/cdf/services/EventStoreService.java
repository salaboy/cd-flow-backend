package com.salaboy.cdf.services;

import io.cloudevents.CloudEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class EventStoreService {
    private Map<String, List<CloudEvent>> eventsPerModule = new ConcurrentHashMap<>();

    public void addEventToModule(CloudEvent cloudEvent) {
        if(cloudEvent.getType().equals("CDF.Project.Created")){ // ignore for now
            return;
        }

        String moduleName = cloudEvent.getExtension("cdfmodulename").toString();
        if (eventsPerModule.get(moduleName) == null) {
            eventsPerModule.put(moduleName, new CopyOnWriteArrayList<>());
        }
        eventsPerModule.get(moduleName).add(cloudEvent);

    }

    public List<CloudEvent> getEventsForModule(String moduleName) {
        return eventsPerModule.get(moduleName);
    }


    public Map<String, List<CloudEvent>> getAllEvents(){
        return eventsPerModule;
    }
}
