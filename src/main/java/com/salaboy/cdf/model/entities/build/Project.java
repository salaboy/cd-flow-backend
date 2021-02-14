package com.salaboy.cdf.model.entities.build;

import com.salaboy.cdf.model.entities.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(ProjectId.class)
public class Project extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    @OrderBy("last_modified_date DESC")
    private Set<Module> modules;


    public Project() {
    }

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
