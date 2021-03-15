package com.salaboy.cdf.prometheus;

import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.Service;
import com.salaboy.cdf.services.RuntimeService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class PrometheusServiceMetrics {
    private final MeterRegistry meterRegistry;

    @Autowired
    private RuntimeService runtimeService;


    public PrometheusServiceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public List<String> createServiceStatusGauge(String serviceName, String serviceVersion) {
        return meterRegistry.gauge(serviceName + "_Gauge", Tags.of("version", serviceVersion), new ArrayList<>(), List::size);
    }

    // Gather data about the Environment for prometheus.. this should be something like:
    // Environment A - UP
    // Environment A - Service A - V1.0.0 UP (ON/OFF - 0/1)
    // Environment A - Service A - V1.0.1 UP (ON/OFF - 0/1)
    // Environment A - Service B - V2.0.3 DOWN
    // Environment B - UP
    @Scheduled(fixedRate = 5000)
    public void gatherData() {

        Iterable<Environment> environments = runtimeService.getEnvironments();
        System.out.println("Envionments: ");
        for (Environment env : environments) {
            System.out.println("\t Environment: " + env.getName());
            System.out.println("\t Services: ");
            Set<Service> services = env.getServices();
            for (Service service : services) {
                System.out.println("\t\t Service: " + service.getName() + " Version:" + service.getVersion());
                List<String> serviceStatusGauge = createServiceStatusGauge(service.getName(), service.getVersion());
                serviceStatusGauge.add(service.getName());
            }
        }

    }

}
