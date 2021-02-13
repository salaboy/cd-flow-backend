package com.salaboy.cdf.model.entities.run;

import com.salaboy.cdf.model.entities.Auditable;
import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.ProjectId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(EnvironmentId.class)
public class Environment extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private String name;

    private String tags;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "environment")
    @OrderBy("last_modified_date ASC")
    private Set<Service> services;
    private String repoURL;

    public Environment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public void setRepoURL(String repoURL) {
        this.repoURL = repoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void addService(Service service) {
        if(services == null){
            services = new HashSet<>();
        }
        services.add(service);
    }
}
