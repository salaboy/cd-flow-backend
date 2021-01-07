package com.salaboy.cdf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PipelineRun {
    private String id;
    private Module module;
    private String status;
    private List<ArtifactEvent> artifactEvents = new ArrayList<>();
    private Date createdDate;
    private Date lastModified;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArtifactEvent> getArtifactEvents() {
        return artifactEvents;
    }

    public void setArtifactEvents(List<ArtifactEvent> artifactEvents) {
        this.artifactEvents = artifactEvents;
    }

    public void addArtifactEvent(ArtifactEvent artifactEvent) {
        this.artifactEvents.add(artifactEvent);
    }
}
