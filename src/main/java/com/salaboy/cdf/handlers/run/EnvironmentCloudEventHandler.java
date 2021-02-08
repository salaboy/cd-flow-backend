package com.salaboy.cdf.handlers.run;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.services.BuildTimeService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class EnvironmentCloudEventHandler implements CloudEventHandler {


    @Autowired
    private BuildTimeService projectService;

    public EnvironmentCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String projectName = "";
        if (ce.getExtension("cdfenvname") != null) {
            projectName = ce.getExtension("cdfenvname").toString();

            if (ce.getType().equals("CDF.Environment.Created")) {

                Environment project = new Project();
                project.setName(projectName);

                projectService.addOrUpdateProject(project);
                log.info("Environment Created: " + projectName);
                return;
            }

            if (ce.getType().equals("CDF.Project.Deleted")) {
                Optional<Project> projectByName = projectService.getProjectByName(projectName);
                if (projectByName.isPresent()) {
                    projectService.deleteProject(projectByName.get());
                    log.info("Project Deleted: " + projectName);
                }
                return;
            }
        } else {
            log.error("Environment Name not available in EnvironmentCloudEventHandler: " + projectName);
        }
        return;

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if (ce.getType().equals("CDF.Project.Created") || ce.getType().equals("CDF.Project.Deleted")) {
            return true;
        }
        return false;
    }
}
