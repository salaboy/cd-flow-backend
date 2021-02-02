package com.salaboy.cdf.model.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CloudEventEntity extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String cloudEvent;


    private String moduleName;

    private String type;


    public CloudEventEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCloudEvent() {
        return cloudEvent;
    }

    public void setCloudEvent(String cloudEvent) {
        this.cloudEvent = cloudEvent;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
