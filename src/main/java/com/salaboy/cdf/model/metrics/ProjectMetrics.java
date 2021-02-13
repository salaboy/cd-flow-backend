package com.salaboy.cdf.model.metrics;

import java.util.ArrayList;
import java.util.List;

public class ProjectMetrics {
    private String projectName;
    private List<ModuleMetrics> moduleMetrics = new ArrayList<>();


    public ProjectMetrics() {
    }

    public ProjectMetrics(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<ModuleMetrics> getModuleMetrics() {
        return moduleMetrics;
    }

    public void setModuleMetrics(List<ModuleMetrics> moduleMetrics) {
        this.moduleMetrics = moduleMetrics;
    }

    public void addModuleMetrics(ModuleMetrics moduleMetrics) {
        this.moduleMetrics.add(moduleMetrics);
    }
}
