package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;

import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.entities.Project;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
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
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String moduleRepo = ce.getExtension("cdfmodulerepo").toString();

        String projectName = ce.getExtension("cdfprojectname").toString();

        if (ce.getType().equals("CDF.Module.Created")) {



            Optional<Project> projectByName = projectService.getProjectByName(projectName);
            if(projectByName.isPresent()) {
                Module module = new Module();
                module.setName(moduleName);
                module.setRepoUrl(moduleRepo);
                Project project = projectByName.get();
                project.addModule(module);
                module.setProject(project);
                projectService.addOrUpdateModule(module);
                projectService.addOrUpdateProject(project);

            }else{
               log.error("No project by name: " + projectName);
            }

            return;
        }

        if (ce.getType().equals("CDF.Module.Deleted")) {
            Optional<Project> projectByName = projectService.getProjectByName(projectName);
            if(projectByName.isPresent()){
                Optional<Module> moduleByName = projectService.getModuleByName(moduleName);
                if(moduleByName.isPresent()){
                    projectService.deleteModuleFromProject(projectByName.get(), moduleByName.get());
                }

            }
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
