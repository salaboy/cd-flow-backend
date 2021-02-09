package com.salaboy.cdf.handlers.build;

import com.salaboy.cdf.CloudEventHandler;

import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.services.BuildTimeService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ModuleCloudEventHandler implements CloudEventHandler {

//    	event.SetExtension("cdfmodulename", moduleName)
//            event.SetExtension("cdfmodulerepo", moduleRepository)
//            event.SetExtension("cdfmoduleprojectname", moduleProjectName)

    @Autowired
    private BuildTimeService buildTimeService;

    public ModuleCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String moduleName = ce.getExtension("cdfmodulename").toString();
        String moduleRepo = ce.getExtension("cdfmodulerepo").toString();

        String projectName = ce.getExtension("cdfprojectname").toString();

        if (ce.getType().equals("CDF.Module.Created")) {

            List<Project> projectsByName = buildTimeService.getProjectByName(projectName);
            if(!projectsByName.isEmpty()) {
                Module module = new Module();
                module.setName(moduleName);
                module.setRepoUrl(moduleRepo);
                Project project = projectsByName.get(0);
                module.setProject(project);
                Module savedModule = buildTimeService.addOrUpdateModule(module);
                project.addModule(savedModule);
                buildTimeService.addOrUpdateProject(project);

            }

            return;
        }

        if (ce.getType().equals("CDF.Module.Deleted")) {
            List<Project> projectsByName = buildTimeService.getProjectByName(projectName);
            if(!projectsByName.isEmpty()) {
                Optional<Module> modulesByName = buildTimeService.getModuleByName(projectName, moduleName);
                if(!modulesByName.isPresent()){
                    buildTimeService.deleteModuleFromProject(projectsByName.get(0), modulesByName.get());
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
