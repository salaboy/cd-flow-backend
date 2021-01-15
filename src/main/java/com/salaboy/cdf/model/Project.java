package com.salaboy.cdf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Project {
    private String id;
    private String name;
    private List<Module> modules = new ArrayList<>();
    private Date createdDate;
    private Date lastModified;

    public Project() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Date();
        this.lastModified = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.lastModified = new Date();
        this.name = name;
    }

    public List<Module> getModules() {
        return modules;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void addModule(Module module) {
        Module moduleByName = getModuleByName(module.getName());
        if (moduleByName == null) {
            this.lastModified = new Date();
            this.modules.add(module);
        }
    }

    private Module getModuleByName(String moduleName) {
        for (Module m : modules) {
            if (m.getName().equals(moduleName)) {
                return m;
            }
        }
        return null;
    }
}
