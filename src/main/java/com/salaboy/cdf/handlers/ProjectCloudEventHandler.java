package com.salaboy.cdf.handlers;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.Project;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectCloudEventHandler implements CloudEventHandler {

    	//event.SetExtension("cdfprojectname", projectName)

    @Autowired
    private ProjectService projectService;

    public ProjectCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        if(ce.getType().equals("CDF.Project.Created")){
            Project project = new Project();
            project.setName(ce.getExtension("cdfprojectname").toString());
            projectService.addProject(project);
            return;
        }

        if(ce.getType().equals("CDF.Project.Deleted")){

            return;
        }

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if(ce.getType().equals("CDF.Project.Created") || ce.getType().equals("CDF.Project.Deleted")){
            return true;
        }
        return false;
    }
}
