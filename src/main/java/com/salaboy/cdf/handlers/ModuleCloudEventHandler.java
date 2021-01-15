package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;

import com.salaboy.cdf.model.Module;
import com.salaboy.cdf.model.Project;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleCloudEventHandler implements CloudEventHandler {

//    	event.SetExtension("cdfmodulename", moduleName)
//            event.SetExtension("cdfmodulerepo", moduleRepository)
//            event.SetExtension("cdfmoduleprojectname", moduleProjectName)

    @Autowired
    private ProjectService projectService;

    public ModuleCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        if (ce.getType().equals("CDF.Module.Created")) {
            Module module = new Module();
            String moduleName = ce.getExtension("cdfmodulename").toString();
            String moduleRepo = ce.getExtension("cdfmodulerepo").toString();
            module.setName(moduleName);
            module.setRepoUrl(moduleRepo);
            String projectName = ce.getExtension("cdfprojectname").toString();

            Project projectByName = projectService.getProjectByName(projectName);
            projectByName.addModule(module);
            return;
        }

        if (ce.getType().equals("CDF.Module.Deleted")) {

            return;
        }

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if (ce.getType().equals("CDF.Module.Created") || ce.getType().equals("CDF.Module.Deleted")) {
            return true;
        }
        return false;
    }
}
