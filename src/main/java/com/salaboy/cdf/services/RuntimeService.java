package com.salaboy.cdf.services;

import com.salaboy.cdf.model.dao.EnvironmentRepository;
import com.salaboy.cdf.model.dao.ServiceRepository;
import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RuntimeService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;


    public void addOrUpdateEnvironment(Environment environment) {
        environmentRepository.save(environment);
    }

    public List<Environment> getEnvironmentByName(String environmentName) {
        return environmentRepository.findByName(environmentName);
    }

    public void deleteEnvironment(Environment environment) {
        environmentRepository.delete(environment);
    }

    public Service addOrUpdateService(Service service) {
        return serviceRepository.save(service);
    }

    public Optional<Service> getServiceByName(String environmentByName, String serviceName) {
        return serviceRepository.findByName(environmentByName, serviceName);
    }

    public void deleteServiceFromEnvironment(Environment environment, Service service) {
        environment.getServices().remove(service);
        environmentRepository.save(environment);
        serviceRepository.delete(service);
    }
}
