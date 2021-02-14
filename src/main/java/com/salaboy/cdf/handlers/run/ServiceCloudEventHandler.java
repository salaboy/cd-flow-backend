package com.salaboy.cdf.handlers.run;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.Service;
import com.salaboy.cdf.services.RuntimeService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ServiceCloudEventHandler implements CloudEventHandler {


    @Autowired
    private RuntimeService runtimeService;

    public ServiceCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String environmentName = "";
        if (ce.getExtension("cdfenvname") != null) {
            environmentName = ce.getExtension("cdfenvname").toString();
            String serviceName = ce.getExtension("cdfservicename").toString();
            String serviceVersion = ce.getExtension("cdfserviceversion").toString();
            if (ce.getType().equals("CDF.Service.Created")) {

                List<Environment> envsByName = runtimeService.getEnvironmentByName(environmentName);
                if(!envsByName.isEmpty()) {

                    Service service = new Service();
                    service.setName(serviceName);
                    service.setVersion(serviceVersion);
                    Environment environment = envsByName.get(0);
                    service.setEnvironment(environment);
                    Service savedService = runtimeService.addOrUpdateService(service);
                    environment.addService(savedService);
                    runtimeService.addOrUpdateEnvironment(environment);
                }
                return;

            }

            if (ce.getType().equals("CDF.Service.Updated")) {

                Environment environment = new Environment();
                environment.setName(environmentName);

                runtimeService.addOrUpdateEnvironment(environment);
                log.info("Environment Created: " + environmentName);
                return;
            }
            if (ce.getType().equals("CDF.Service.Deleted")) {
                List<Environment> environmentByName = runtimeService.getEnvironmentByName(environmentName);
                if(!environmentByName.isEmpty()) {
                    Optional<Service> servicesByName = runtimeService.getServiceByName(environmentName, serviceName);
                    if(servicesByName.isPresent()){
                        runtimeService.deleteServiceFromEnvironment(environmentByName.get(0), servicesByName.get());
                    }

                }
                return;
            }
        } else {
            log.error("Service Name not available in ServiceCloudEventHandler: " + environmentName);
        }
        return;

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if (ce.getType().equals("CDF.Service.Created") || ce.getType().equals("CDF.Service.Deleted") || ce.getType().equals("CDF.Service.Updated")) {
            return true;
        }
        return false;
    }
}
