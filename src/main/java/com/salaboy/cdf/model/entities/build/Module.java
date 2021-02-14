package com.salaboy.cdf.model.entities.build;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salaboy.cdf.model.entities.Auditable;

import javax.persistence.*;
import java.util.Set;

@Entity
@IdClass(ModuleId.class)
public class Module extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "project_id", nullable = false),
            @JoinColumn(name = "project_name", nullable = false)
    })
    private Project project;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
    @OrderBy("last_modified_date DESC")
    private Set<PipelineRun> pipelineRuns;


    private String repoUrl;
    private String latestVersion;


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

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addPipelineRun(PipelineRun pipelineRun) {
        pipelineRuns.add(pipelineRun);
    }

    public Set<PipelineRun> getPipelineRuns() {
        return pipelineRuns;
    }

    public void setPipelineRuns(Set<PipelineRun> pipelineRuns) {
        this.pipelineRuns = pipelineRuns;
    }
}
