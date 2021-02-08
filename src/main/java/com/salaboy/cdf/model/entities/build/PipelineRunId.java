package com.salaboy.cdf.model.entities.build;


import java.io.Serializable;

public class PipelineRunId implements Serializable {
    private Long id;
    private String pipelineId;

    public PipelineRunId() {
    }

    public PipelineRunId(Long id, String pipelineId) {
        this.id = id;
        this.pipelineId = pipelineId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }
}
