package com.salaboy.cdf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Module {
    private String id;
    private String name;
    private String repoUrl;
    private List<PipelineRun> pipelineRuns = new ArrayList<>();
    private String latestVersion;
    private Date createdDate;
    private Date lastModified;

    public Module() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Date();
        this.lastModified = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public List<PipelineRun> getPipelineRuns() {

        return pipelineRuns;
    }

    public PipelineRun getPipelineRunById(String pipelineId) {
        for (PipelineRun pr : pipelineRuns) {
            if (pr.getId().equals(pipelineId)) {
                return pr;
            }
        }
        return null;
    }

    public void setPipelineRuns(List<PipelineRun> pipelineRuns) {
        this.pipelineRuns = pipelineRuns;
    }

    public void addPipelineRun(PipelineRun pipelineRun) {
        this.lastModified = new Date();
        this.pipelineRuns.add(pipelineRun);
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
