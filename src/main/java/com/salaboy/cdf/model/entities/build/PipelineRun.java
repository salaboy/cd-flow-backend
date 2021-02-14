package com.salaboy.cdf.model.entities.build;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salaboy.cdf.model.entities.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(PipelineRunId.class)
public class PipelineRun extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private String pipelineId;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "module_id", nullable = false),
            @JoinColumn(name = "module_name", nullable = false)
    })

    private Module module;

    private String status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pipelineRun")
    @OrderBy("last_modified_date DESC")
    private Set<ArtifactEvent> artifactEvents;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addArtifactEvent(ArtifactEvent artifactEvent) {
        if (artifactEvents == null) {
            artifactEvents = new HashSet<>();
        }
        artifactEvents.add(artifactEvent);
    }

    public Set<ArtifactEvent> getArtifactEvents() {
        return artifactEvents;
    }

    public void setArtifactEvents(Set<ArtifactEvent> artifactEvents) {
        this.artifactEvents = artifactEvents;
    }
}
