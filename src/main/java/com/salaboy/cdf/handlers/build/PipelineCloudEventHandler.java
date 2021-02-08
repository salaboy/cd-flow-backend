package com.salaboy.cdf.handlers.build;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.services.BuildTimeService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PipelineCloudEventHandler implements CloudEventHandler {


    @Autowired
    private BuildTimeService buildTimeService;

    public PipelineCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String pipelineId = ce.getExtension("cdfpipeid").toString();
        if(ce.getType().equals("CDF.Pipeline.Started")){

            Optional<Module> moduleOptional = buildTimeService.getModuleByName( moduleName);
            PipelineRun pipelineRun = new PipelineRun();
            pipelineRun.setStatus("STARTED");
            pipelineRun.setPipelineId(pipelineId);
            if(moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                module.addPipelineRun(pipelineRun);
                pipelineRun.setModule(module);
                buildTimeService.addOrUpdateModule(module);
            }

            buildTimeService.addOrUpdatePipelineRun(pipelineRun);
            return;
        }

        if(ce.getType().equals("CDF.Pipeline.Finished")){

            Optional<Module> moduleOptional = buildTimeService.getModuleByName( moduleName);
            if(moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                List<PipelineRun> pipelineRunByPipelineId = buildTimeService.findPipelineRunById(pipelineId);
                for(PipelineRun pr: pipelineRunByPipelineId){
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
