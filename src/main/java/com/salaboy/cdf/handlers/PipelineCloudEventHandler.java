package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.entities.PipelineRun;
import com.salaboy.cdf.model.entities.Project;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PipelineCloudEventHandler implements CloudEventHandler {


    @Autowired
    private ProjectService projectService;

    public PipelineCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String pipelineId = ce.getExtension("cdfpipeid").toString();
        if(ce.getType().equals("CDF.Pipeline.Started")){



            Optional<Module> moduleOptional = projectService.getModuleByName( moduleName);
            PipelineRun pipelineRun = new PipelineRun();
            pipelineRun.setStatus("STARTED");
            pipelineRun.setPipelineId(pipelineId);
            if(moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                module.addPipelineRun(pipelineRun);
                pipelineRun.setModule(module);
                projectService.addOrUpdateModule(module);
            }

            projectService.addOrUpdatePipelineRun(pipelineRun);
            return;
        }

        if(ce.getType().equals("CDF.Pipeline.Finished")){

            Optional<Module> moduleOptional = projectService.getModuleByName( moduleName);
            if(moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                Optional<PipelineRun> pipelineRunByIdOptional = projectService.findPipelineRunById(pipelineId);
                if(pipelineRunByIdOptional.isPresent()){
                    PipelineRun pipelineRun = pipelineRunByIdOptional.get();
                    pipelineRun.setStatus("Finished");
                    pipelineRun.setModule(module);
                    projectService.addOrUpdatePipelineRun(pipelineRun);
                }


                return;
            }
        }

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if(ce.getType().equals("CDF.Pipeline.Started") || ce.getType().equals("CDF.Pipeline.Finished") || ce.getType().equals("CDF.Pipeline.Failed")){
            return true;
        }
        return false;
    }
}
