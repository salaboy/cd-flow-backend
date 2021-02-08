package com.salaboy.cdf.model.entities.build;

import com.salaboy.cdf.model.entities.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String moduleId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    @OrderBy("last_modified_date ASC")
    private Set<Module> modules;
    private String name;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }


    public void addModule(Module module) {
        if(modules == null){
            modules = new HashSet<>();
        }
        modules.add(module);
    }
}
