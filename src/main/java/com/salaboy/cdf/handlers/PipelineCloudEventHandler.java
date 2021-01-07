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
//    event.SetExtension("cdfpipeid", pipelineId)
//            event.SetExtension("cdfpipename", pipelineName)
//            event.SetExtension("cdfpipetype", pipelineType)
//            event.SetExtension("cdfpipemodulename", pipelineModuleName)
//            event.SetExtension("cdfpipeenvname", pipelineEnvName)
//            event.SetExtension("cdfpipebranch", pipelineBranch)

    @Autowired
    private ProjectService projectService;

    public PipelineCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        if(ce.getType().equals("CDF.Pipeline.Started")){
            String moduleName = ce.getExtension("cdfpipemodulename").toString();
            String pipelineId = ce.getExtension("cdfpipeid").toString();


            Module module = projectService.getModuleByName( moduleName);
            PipelineRun pipelineRun = new PipelineRun();
            pipelineRun.setStatus("STARTED");
            pipelineRun.setId(pipelineId);

            module.addPipelineRun(pipelineRun);
            return;
        }

        if(ce.getType().equals("CDF.Pipeline.Finished")){

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
