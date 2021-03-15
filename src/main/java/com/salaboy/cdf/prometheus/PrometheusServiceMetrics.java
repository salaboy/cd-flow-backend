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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableScheduling
public class PrometheusServiceMetrics {
    private final MeterRegistry meterRegistry;

    @Autowired
    private RuntimeService runtimeService;

    private Map<String, List<String>> serviceStatusGauges = new ConcurrentHashMap<>();
    private Map<String, List<String>> envStatusGauges = new ConcurrentHashMap<>();
    private Map<String, List<String>> envServicesGauges = new ConcurrentHashMap<>();


    public PrometheusServiceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public List<String> createServiceStatusGauge(String serviceName, String serviceVersion) {
        return meterRegistry.gauge(serviceName + "_Service_Gauge", Tags.of("version", serviceVersion), new ArrayList<>(), List::size);
    }

    public List<String> createEnvStatusGauge(String envName) {
        return meterRegistry.gauge(envName + "_Env_Gauge", Tags.empty(), new ArrayList<>(), List::size);
    }

    public List<String> createEnvServicesGauge(String envName) {
        return meterRegistry.gauge(envName + "_Env_Services_Gauge", Tags.empty(), new ArrayList<>(), List::size);
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
            if(envStatusGauges.get(env.getName()) == null){
                List<String> envStatusGauge = createEnvStatusGauge(env.getName());
                envStatusGauge.add(env.getName());
                envStatusGauges.put(env.getName(), envStatusGauge);
            }

            if(envServicesGauges.get(env.getName()) == null){
                List<String> envServicesGauge = createEnvServicesGauge(env.getName());
                envServicesGauge.add(env.getName());
                envServicesGauges.put(env.getName(), envServicesGauge);
            }
            List<String> envServicesList = envServicesGauges.get(env.getName());
            System.out.println("\t\t Services: ");
            Set<Service> services = env.getServices();
            for (Service service : services) {
                System.out.println("\t\t\t Service: " + service.getName() + " Version:" + service.getVersion());
                envServicesList.add(service.getName());
                if(serviceStatusGauges.get(service.getName() + "-" + service.getVersion()) == null) {
                    System.out.println("Gauge for: " + service.getName() + "-" + service.getVersion() + " didn't existed");
                    List<String> serviceStatusGauge = createServiceStatusGauge(service.getName(), service.getVersion());
                    serviceStatusGauge.add(service.getName());
                    serviceStatusGauges.put(service.getName() + "-" + service.getVersion(), serviceStatusGauge);
                }else{
                    System.out.println("Gauge for: " + service.getName() + "-" + service.getVersion() +
                            " existed already, with size: " + serviceStatusGauges.get(service.getName() + "-" + service.getVersion()).size());

                }
            }
        }

    }

}
