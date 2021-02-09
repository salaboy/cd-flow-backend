package com.salaboy.cdf.handlers.build;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.services.BuildTimeService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PipelineRunCloudEventHandler implements CloudEventHandler {


    @Autowired
    private BuildTimeService buildTimeService;

    public PipelineRunCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String projectName = ce.getExtension("cdfprojectname").toString();
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String pipelineId = ce.getExtension("cdfpipeid").toString();
        if(ce.getType().equals("CDF.Pipeline.Started")){


            PipelineRun pipelineRun = new PipelineRun();
            pipelineRun.setStatus("STARTED");
            pipelineRun.setPipelineId(pipelineId);
            List<Module> modulesByName = buildTimeService.getModuleByName( projectName, moduleName);
            if(!modulesByName.isEmpty()) {
                Module module = modulesByName.get(0);
                pipelineRun.setModule(module);
                PipelineRun savedPipelineRun = buildTimeService.addOrUpdatePipelineRun(pipelineRun);

                module.addPipelineRun(savedPipelineRun);
                buildTimeService.addOrUpdateModule(module);
            }






            return;
        }

        if(ce.getType().equals("CDF.Pipeline.Finished")){

            List<Module> modulesByName = buildTimeService.getModuleByName(projectName,  moduleName);
            if(!modulesByName.isEmpty()) {
                Module module = modulesByName.get(0);
                List<PipelineRun> pipelineRunsByPipelineId = buildTimeService.findPipelineRunById(pipelineId);
                if(!pipelineRunsByPipelineId.isEmpty()){
                    PipelineRun pr = pipelineRunsByPipelineId.get(0);
                    pr.setStatus("FINISHED");
                    pr.setModule(module);
                    buildTimeService.addOrUpdatePipelineRun(pr);
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
