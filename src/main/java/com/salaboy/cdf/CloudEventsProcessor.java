package com.salaboy.cdf;

import com.salaboy.cdf.handlers.build.ArtifactCloudEventHandler;
import com.salaboy.cdf.handlers.build.ModuleCloudEventHandler;
import com.salaboy.cdf.handlers.build.PipelineRunCloudEventHandler;
import com.salaboy.cdf.handlers.build.ProjectCloudEventHandler;
import com.salaboy.cdf.handlers.run.EnvironmentCloudEventHandler;
import com.salaboy.cdf.handlers.run.ServiceCloudEventHandler;
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
    private PipelineRunCloudEventHandler pipelineRunCloudEventHandler;

    @Autowired
    private ArtifactCloudEventHandler artifactCloudEventHandler;

    @Autowired
    private EnvironmentCloudEventHandler environmentCloudEventHandler;

    @Autowired
    private ServiceCloudEventHandler serviceCloudEventHandler;

    @PostConstruct
    public void init() {
        handlers.add(projectCloudEventHandler);
        handlers.add(moduleCloudEventHandler);
        handlers.add(pipelineRunCloudEventHandler);
        handlers.add(artifactCloudEventHandler);
        handlers.add(environmentCloudEventHandler);
        handlers.add(serviceCloudEventHandler);
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
