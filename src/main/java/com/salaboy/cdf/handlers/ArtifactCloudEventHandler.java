package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.ArtifactEvent;
import com.salaboy.cdf.model.Module;
import com.salaboy.cdf.model.PipelineRun;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtifactCloudEventHandler implements CloudEventHandler {

//            event.SetExtension("cdfartifactmodule", artifactModuleName)
//            event.SetExtension("cdfartifactpipeid", artifactPipelineId)
//            event.SetExtension("cdfartifactid", artifactId)

    @Autowired
    private ProjectService projectService;

    public ArtifactCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String moduleName = ce.getExtension("cdfartifactmodule").toString();
        String artifactId = ce.getExtension("cdfartifactid").toString();
        String pipelineId = ce.getExtension("cdfartifactpipeid").toString();
        if(ce.getType().equals("CDF.Artifact.Built")){
            PipelineRun pipelineRun = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            ArtifactEvent artifactEvent = new ArtifactEvent();
            artifactEvent.setId(artifactId);
            artifactEvent.setType("BUILT");
            pipelineRun.addArtifactEvent(artifactEvent);
            return;
        }

        if(ce.getType().equals("CDF.Artifact.TestsStarted")){
            PipelineRun pipelineRun = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            ArtifactEvent artifactEvent = new ArtifactEvent();
            artifactEvent.setId(artifactId);
            artifactEvent.setType("TEST_STARTED");
            pipelineRun.addArtifactEvent(artifactEvent);
            return;
        }

        if(ce.getType().equals("CDF.Artifact.TestsEnded")){
            PipelineRun pipelineRun = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            ArtifactEvent artifactEvent = new ArtifactEvent();
            artifactEvent.setId(artifactId);
            artifactEvent.setType("TEST_ENDED");
            pipelineRun.addArtifactEvent(artifactEvent);
            return;
        }

        if(ce.getType().equals("CDF.Artifact.Released")){
            PipelineRun pipelineRun = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            ArtifactEvent artifactEvent = new ArtifactEvent();
            artifactEvent.setId(artifactId);
            artifactEvent.setType("RELEASED");
            pipelineRun.addArtifactEvent(artifactEvent);
            return;
        }

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if(ce.getType().equals("CDF.Artifact.Released") || ce.getType().equals("CDF.Artifact.TestsStarted")
                || ce.getType().equals("CDF.Artifact.TestsEnded") || ce.getType().equals("CDF.Artifact.Built")){
            return true;
        }
        return false;
    }
}
