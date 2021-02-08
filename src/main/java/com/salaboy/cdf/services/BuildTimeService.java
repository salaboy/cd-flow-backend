package com.salaboy.cdf.services;

import com.salaboy.cdf.model.dao.ArtifactEventsRepository;
import com.salaboy.cdf.model.dao.ModuleRepository;
import com.salaboy.cdf.model.dao.PipelineRunRepository;
import com.salaboy.cdf.model.dao.ProjectRepository;
import com.salaboy.cdf.model.entities.build.ArtifactEvent;
import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.model.entities.build.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class BuildTimeService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private PipelineRunRepository pipelineRunRepository;

    @Autowired
    private ArtifactEventsRepository artifactEventsRepository;

    public BuildTimeService() {
    }


    public Iterable<Project> getProjects() {
        return projectRepository.findAll(Sort.by(Sort.Direction.ASC, "lastModifiedDate"));
    }

    public void addOrUpdateProject(Project project) {
        projectRepository.save(project);
    }


    public Optional<Project> getProjectByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    public Optional<Module> getModuleFromProject(String projectName, String moduleName) {

        Optional<Project> projectByName = getProjectByName(projectName);
        if (projectByName.isPresent()) {
            return moduleRepository.getModuleFromProject(projectByName.get(), moduleName);
        }
        return null;
    }

    public Optional<PipelineRun> getPipelineRunFromModule(String moduleName, String pipelineId) {
        return pipelineRunRepository.findByModuleAndId(moduleName, pipelineId);

    }


    public Optional<Module> getModuleByName(String moduleName) {
        return moduleRepository.findByName(moduleName);
    }

    public Optional<PipelineRun> findPipelineRunById(String pipelineId) {
        return pipelineRunRepository.findByPipelineId(pipelineId);
    }

    public void addOrUpdatePipelineRun(PipelineRun pipelineRun) {
        pipelineRunRepository.save(pipelineRun);
    }

    public void addOrUpdateModule(Module module) {
        moduleRepository.save(module);
    }

    public void addOrUpdateArtifactEvent(ArtifactEvent artifactEvent) {
        artifactEventsRepository.save(artifactEvent);
    }


    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    public void deleteModuleFromProject(Project project, Module module) {
        project.getModules().remove(module);
        projectRepository.save(project);
        moduleRepository.delete(module);
    }
}
