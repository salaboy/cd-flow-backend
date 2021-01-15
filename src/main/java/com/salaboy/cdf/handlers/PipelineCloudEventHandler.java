package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.Module;
import com.salaboy.cdf.model.PipelineRun;
import com.salaboy.cdf.model.Project;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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



            Module module = projectService.getModuleByName( moduleName);
            PipelineRun pipelineRun = new PipelineRun();
            pipelineRun.setStatus("STARTED");
            pipelineRun.setId(pipelineId);

            module.addPipelineRun(pipelineRun);
            return;
        }

        if(ce.getType().equals("CDF.Pipeline.Finished")){

            Module module = projectService.getModuleByName( moduleName);

            PipelineRun pipelineRunById = module.getPipelineRunById(pipelineId);
            pipelineRunById.setStatus("Finished");

            return;
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
