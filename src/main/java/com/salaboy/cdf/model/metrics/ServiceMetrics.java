package com.salaboy.cdf.model.metrics;

public class ServiceMetrics {
    private String serviceName;
    private String version;
    private String artifact;

    public ServiceMetrics() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }
}
