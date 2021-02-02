package com.salaboy.cdf.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ArtifactEvent extends Auditable<String> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String artifactId;

    private String type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="pipelineRun", nullable=false)
    private PipelineRun pipelineRun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PipelineRun getPipelineRun() {
        return pipelineRun;
    }

    public void setPipelineRun(PipelineRun pipelineRun) {
        this.pipelineRun = pipelineRun;
    }
}
