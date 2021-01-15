package com.salaboy.cdf.model.metrics;

public class PipelineMetrics {
    private String pipelineId;

    private String buildTime;
    private String releaseTime;
    private String testsTime;
    private String pipelineTime;

    public PipelineMetrics() {
    }

    public PipelineMetrics(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getTestsTime() {
        return testsTime;
    }

    public void setTestsTime(String testsTime) {
        this.testsTime = testsTime;
    }

    public String getPipelineTime() {
        return pipelineTime;
    }

    public void setPipelineTime(String pipelineTime) {
        this.pipelineTime = pipelineTime;
    }
}
