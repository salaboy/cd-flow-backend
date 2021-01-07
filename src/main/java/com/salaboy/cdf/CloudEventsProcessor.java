package com.salaboy.cdf;

import com.salaboy.cdf.handlers.ArtifactCloudEventHandler;
import com.salaboy.cdf.handlers.ModuleCloudEventHandler;
import com.salaboy.cdf.handlers.PipelineCloudEventHandler;
import com.salaboy.cdf.handlers.ProjectCloudEventHandler;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CloudEventsProcessor {
    private List<CloudEventHandler> handlers = new ArrayList<>();

    @Autowired
    private ProjectCloudEventHandler projectCloudEventHandler;

    @Autowired
    private ModuleCloudEventHandler moduleCloudEventHandler;

    @Autowired
    private PipelineCloudEventHandler pipelineCloudEventHandler;

    @Autowired
    private ArtifactCloudEventHandler artifactCloudEventHandler;

    @PostConstruct
    public void init() {
        handlers.add(projectCloudEventHandler);
        handlers.add(moduleCloudEventHandler);
        handlers.add(pipelineCloudEventHandler);
        handlers.add(artifactCloudEventHandler);
    }

    public void handleEvent(CloudEvent cloudEvent){
        for(CloudEventHandler h : handlers){
            if(h.matches(cloudEvent)){
                h.handle(cloudEvent);
                return;
            }
        }

    }

}
