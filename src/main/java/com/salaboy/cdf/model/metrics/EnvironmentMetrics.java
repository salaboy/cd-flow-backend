package com.salaboy.cdf.model.metrics;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentMetrics {
    private String environmentName;

    private List<ServiceMetrics> serviceMetrics = new ArrayList<>();

    public EnvironmentMetrics() {
    }


    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public List<ServiceMetrics> getServiceMetrics() {
        return serviceMetrics;
    }

    public void addServiceMetrics(ServiceMetrics sm) {
        this.serviceMetrics.add(sm);
    }

    public void setServiceMetrics(List<ServiceMetrics> serviceMetrics) {
        this.serviceMetrics = serviceMetrics;
    }
}
