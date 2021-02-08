package com.salaboy.cdf.handlers.build;

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
public class ProjectCloudEventHandler implements CloudEventHandler {


    @Autowired
    private BuildTimeService buildTimeService;

    public ProjectCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String projectName = "";
        if (ce.getExtension("cdfprojectname") != null) {
            projectName = ce.getExtension("cdfprojectname").toString();

            if (ce.getType().equals("CDF.Project.Created")) {

                Project project = new Project();
                project.setName(projectName);

                buildTimeService.addOrUpdateProject(project);
                log.info("Project Created: " + projectName);
                return;
            }

            if (ce.getType().equals("CDF.Project.Deleted")) {
                Optional<Project> projectByName = buildTimeService.getProjectByName(projectName);
                if (projectByName.isPresent()) {
                    buildTimeService.deleteProject(projectByName.get());
                    log.info("Project Deleted: " + projectName);
                }
                return;
            }
        } else {
            log.error("Project Name not available in ProjectCloudEventHandler: " + projectName);
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
