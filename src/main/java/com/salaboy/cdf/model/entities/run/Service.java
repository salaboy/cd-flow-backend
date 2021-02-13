package com.salaboy.cdf.model.entities.run;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salaboy.cdf.model.entities.Auditable;
import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.model.entities.build.Project;

import javax.persistence.*;
import java.util.Set;

@Entity
@IdClass(ServiceId.class)
public class Service extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "environment_id", nullable = false),
            @JoinColumn(name = "environment_name", nullable = false)
    })
    private Environment environment;


    private String repoUrl;
    private String version;
    private String serviceId;
    private String nodeName;
    private String namespaceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
