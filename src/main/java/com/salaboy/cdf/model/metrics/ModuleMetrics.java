package com.salaboy.cdf.model.metrics;

import java.util.ArrayList;
import java.util.List;

public class ModuleMetrics {

    private String projectName;
    private String moduleName;
    private String repoUrl;

    private List<PipelineMetrics> pipelineMetrics = new ArrayList<>();

    public ModuleMetrics(String projectName, String moduleName, String repoUrl) {
        this.projectName = projectName;
        this.moduleName = moduleName;
        this.repoUrl = repoUrl;
    }

    public ModuleMetrics() {
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<PipelineMetrics> getPipelineMetrics() {
        return pipelineMetrics;
    }

    public void setPipelineMetrics(List<PipelineMetrics> pipelineMetrics) {
        this.pipelineMetrics = pipelineMetrics;
    }

    public void addPipeleinMetric(PipelineMetrics pipelineMetrics) {
        this.pipelineMetrics.add(pipelineMetrics);
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}
