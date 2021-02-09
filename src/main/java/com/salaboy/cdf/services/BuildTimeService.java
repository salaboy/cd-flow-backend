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

import java.util.List;
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

    public Project addOrUpdateProject(Project project) {
        return projectRepository.save(project);
    }


    public List<Project> getProjectByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    public Optional<Module> getModuleByName(String projectName, String moduleName) {
        return moduleRepository.findByName(projectName, moduleName);
    }

    public Optional<PipelineRun> findPipelineRunById(String projectName, String moduleName, String pipelineId) {
        return pipelineRunRepository.findByPipelineId(projectName, moduleName, pipelineId);
    }

    public PipelineRun addOrUpdatePipelineRun(PipelineRun pipelineRun) {
        return pipelineRunRepository.save(pipelineRun);
    }

    public Module addOrUpdateModule(Module module) {
        return moduleRepository.save(module);
    }

    public ArtifactEvent addOrUpdateArtifactEvent(ArtifactEvent artifactEvent) {
        return artifactEventsRepository.save(artifactEvent);
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
