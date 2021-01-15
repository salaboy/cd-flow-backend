package com.salaboy.cdf.model.metrics;

import java.util.ArrayList;
import java.util.List;

public class ProjectsMetrics {
    private List<ProjectMetrics> projectMetrics = new ArrayList<>();

    public ProjectsMetrics() {
    }

    public List<ProjectMetrics> getProjectMetrics() {
        return projectMetrics;
    }

    public void addProjectMetrics(ProjectMetrics projectMetrics) {
        this.projectMetrics.add(projectMetrics);
    }

    public void setProjectMetrics(List<ProjectMetrics> projectMetrics) {
        this.projectMetrics = projectMetrics;
    }
}
