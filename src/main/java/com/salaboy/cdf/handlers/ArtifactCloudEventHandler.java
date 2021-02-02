package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.ArtifactEvent;
import com.salaboy.cdf.model.entities.PipelineRun;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ArtifactCloudEventHandler implements CloudEventHandler {

    @Autowired
    private ProjectService projectService;

    public ArtifactCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String artifactId = ce.getExtension("cdfartifactid").toString();
        String pipelineId = ce.getExtension("cdfpipeid").toString();
        if(ce.getType().equals("CDF.Artifact.Built")){
            Optional<PipelineRun> pipelineRunOptional = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            if(pipelineRunOptional.isPresent()) {
                PipelineRun pipelineRun = pipelineRunOptional.get();
                ArtifactEvent artifactEvent = new ArtifactEvent();
                artifactEvent.setArtifactId(artifactId);
                artifactEvent.setType("BUILT");
                artifactEvent.setPipelineRun(pipelineRun);
                pipelineRun.addArtifactEvent(artifactEvent);
                projectService.addOrUpdateArtifactEvent(artifactEvent);
                projectService.addOrUpdatePipelineRun(pipelineRun);
            }else{
                log.error("No Pipeline found for module: " + moduleName + " and pipeline id: " + pipelineId);
            }
            return;
        }

        if(ce.getType().equals("CDF.Artifact.TestsStarted")){
            Optional<PipelineRun> pipelineRunOptional = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            if(pipelineRunOptional.isPresent()) {
                PipelineRun pipelineRun = pipelineRunOptional.get();
                ArtifactEvent artifactEvent = new ArtifactEvent();
                artifactEvent.setArtifactId(artifactId);
                artifactEvent.setPipelineRun(pipelineRun);
                artifactEvent.setType("TEST_STARTED");
                pipelineRun.addArtifactEvent(artifactEvent);
                projectService.addOrUpdateArtifactEvent(artifactEvent);
                projectService.addOrUpdatePipelineRun(pipelineRun);
            }else{
                log.error("No Pipeline found for module: " + moduleName + " and pipeline id: " + pipelineId);
            }
            return;
        }

        if(ce.getType().equals("CDF.Artifact.TestsEnded")){
            Optional<PipelineRun> pipelineRunOptional = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            if(pipelineRunOptional.isPresent()) {
                PipelineRun pipelineRun = pipelineRunOptional.get();
                ArtifactEvent artifactEvent = new ArtifactEvent();
                artifactEvent.setArtifactId(artifactId);
                artifactEvent.setType("TEST_ENDED");
                artifactEvent.setPipelineRun(pipelineRun);
                pipelineRun.addArtifactEvent(artifactEvent);
                projectService.addOrUpdateArtifactEvent(artifactEvent);
                projectService.addOrUpdatePipelineRun(pipelineRun);

            }else{
                log.error("No Pipeline found for module: " + moduleName + " and pipeline id: " + pipelineId);
            }
            return;
        }

        if(ce.getType().equals("CDF.Artifact.Released")){
            Optional<PipelineRun> pipelineRunOptional = projectService.getPipelineRunFromModule(moduleName, pipelineId);
            if(pipelineRunOptional.isPresent()) {
                PipelineRun pipelineRun = pipelineRunOptional.get();
                ArtifactEvent artifactEvent = new ArtifactEvent();
                artifactEvent.setArtifactId(artifactId);
                artifactEvent.setType("RELEASED");
                pipelineRun.addArtifactEvent(artifactEvent);
                artifactEvent.setPipelineRun(pipelineRun);
                projectService.addOrUpdateArtifactEvent(artifactEvent);
                projectService.addOrUpdatePipelineRun(pipelineRun);
            }else{
                log.error("No Pipeline found for module: " + moduleName + " and pipeline id: " + pipelineId);
            }
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
