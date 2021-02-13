package com.salaboy.cdf.handlers.run;

import com.salaboy.cdf.CloudEventHandler;
import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.services.RuntimeService;
import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EnvironmentCloudEventHandler implements CloudEventHandler {


    @Autowired
    private RuntimeService runtimeService;

    public EnvironmentCloudEventHandler() {
    }

    @Override
    public void handle(CloudEvent ce) {
        String environmentName = "";
        if (ce.getExtension("cdfenvname") != null) {
            environmentName = ce.getExtension("cdfenvname").toString();

            if (ce.getType().equals("CDF.Environment.Created")) {

                Environment environment = new Environment();
                environment.setName(environmentName);

                runtimeService.addOrUpdateEnvironment(environment);
                log.info("Environment Created: " + environmentName);
                return;
            }

            if (ce.getType().equals("CDF.Environment.Deleted")) {
                List<Environment> envByName = runtimeService.getEnvironmentByName(environmentName);
                if (!envByName.isEmpty()) {
                    runtimeService.deleteEnvironment(envByName.get(0));
                    log.info("Environment Deleted: " + envByName);
                }
                return;
            }
        } else {
            log.error("Environment Name not available in EnvironmentCloudEventHandler: " + environmentName);
        }
        return;

    }

    @Override
    public boolean matches(CloudEvent ce) {
        if (ce.getType().equals("CDF.Environment.Created") || ce.getType().equals("CDF.Environment.Deleted")) {
            return true;
        }
        return false;
    }
}
