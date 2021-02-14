package com.salaboy.cdf.model.metrics;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentsMetrics {
    private List<EnvironmentMetrics> environmentMetrics = new ArrayList<>();

    public List<EnvironmentMetrics> getEnvironmentMetrics() {
        return environmentMetrics;
    }

    public void setEnvironmentMetrics(List<EnvironmentMetrics> environmentMetrics) {
        this.environmentMetrics = environmentMetrics;
    }

    public void addEnvironmentMetrics(EnvironmentMetrics environmentMetrics) {
        this.environmentMetrics.add(environmentMetrics);
    }
}
