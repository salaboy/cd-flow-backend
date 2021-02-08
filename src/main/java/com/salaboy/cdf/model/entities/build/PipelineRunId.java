package com.salaboy.cdf.model.entities.build;


import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipelineRunId that = (PipelineRunId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pipelineId, that.pipelineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pipelineId);
    }
}
