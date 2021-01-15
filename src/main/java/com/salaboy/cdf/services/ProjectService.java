package com.salaboy.cdf.services;

import com.salaboy.cdf.model.Module;
import com.salaboy.cdf.model.PipelineRun;
import com.salaboy.cdf.model.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProjectService {

    public ProjectService() {
    }

    private List<Project> projects = new ArrayList<>();

    public List<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        Project projectByName = getProjectByName(project.getName());
        if (projectByName == null) {
            projects.add(project);
        }

    }


    public Project getProjectByName(String projectName) {
        for (Project p : projects) {
            if (p.getName().equals(projectName)) {
                return p;
            }
        }
        return null;
    }

    public Module getModuleFromProject(String projectName, String moduleName) {
        Project projectByName = getProjectByName(projectName);
        for (Module m : projectByName.getModules()) {
            if (m.getName().equals(moduleName)) {
                return m;
            }
        }
        return null;
    }

    public PipelineRun getPipelineRunFromModule(String moduleName, String pipelineId) {
        Module module = null;
        for (Project p : projects) {
            for (Module m : p.getModules()) {
                if (m.getName().equals(moduleName)) {
                    module = m;
                }
            }
        }
        if (module != null) {
            for (PipelineRun p : module.getPipelineRuns()) {
                if (p.getId().equals(pipelineId)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Module getModuleByName(String moduleName) {
        for (Project p : projects) {
            for (Module m : p.getModules()) {
                if (m.getName().equals(moduleName)) {
                    return m;
                }
            }
        }
        return null;
    }
}
