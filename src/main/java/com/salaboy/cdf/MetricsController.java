package com.salaboy.cdf;

import com.salaboy.cdf.model.entities.*;
import com.salaboy.cdf.model.entities.Module;
import com.salaboy.cdf.model.metrics.ModuleMetrics;
import com.salaboy.cdf.model.metrics.PipelineMetrics;
import com.salaboy.cdf.model.metrics.ProjectMetrics;
import com.salaboy.cdf.model.metrics.ProjectsMetrics;
import com.salaboy.cdf.services.EventStoreService;
import com.salaboy.cdf.services.ProjectService;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/metrics/")
public class MetricsController {

    @Autowired
    private EventStoreService eventStoreService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public ProjectsMetrics getProjectMetrics() {
        ProjectsMetrics projectsMetrics = new ProjectsMetrics();

        Iterable<Project> projects = projectService.getProjects();
        for (Project p : projects) {
            ProjectMetrics projectMetrics = new ProjectMetrics(p.getName());
            for (Module module : p.getModules()) {
                ModuleMetrics moduleMetrics = new ModuleMetrics(p.getName(), module.getName());
                Set<PipelineRun> pipelineRuns = module.getPipelineRuns();
                List<CloudEvent> eventsForModule = eventStoreService.getEventsForModule(module.getName());
                for (PipelineRun pr : pipelineRuns) {
                    PipelineMetrics pipelineMetrics = new PipelineMetrics(pr.getPipelineId());
                    pipelineMetrics.setBuildTime(calculateBuildTime(eventsForModule, pr.getPipelineId()));
                    pipelineMetrics.setTestsTime(calculateTestsTime(eventsForModule, pr.getPipelineId()));
                    pipelineMetrics.setReleaseTime(calculateReleaseTime(eventsForModule, pr.getPipelineId()));
                    pipelineMetrics.setPipelineTime(calculatePipelineTime(eventsForModule, pr.getPipelineId()));
                    moduleMetrics.addPipeleinMetric(pipelineMetrics);
                }
                projectMetrics.addModuleMetrics(moduleMetrics);
            }
            projectsMetrics.addProjectMetrics(projectMetrics);

        }
        return projectsMetrics;
    }

    private String calculateBuildTime(List<CloudEvent> eventsForModule, String pipelineId) {

        if (eventsForModule != null) {
            OffsetDateTime startTime = null;
            OffsetDateTime buildTime = null;
            for (CloudEvent ce : eventsForModule) {
                if (ce.getType().equals("CDF.Pipeline.Started")) {

                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        startTime = ce.getTime();
                    }

                }
                if (ce.getType().equals("CDF.Artifact.Built")) {
                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        buildTime = ce.getTime();
                    }
                }
            }
            if (startTime != null && buildTime != null) {
                Duration duration = Duration.between(startTime, buildTime);

                return duration.toHoursPart() + "hh : " + duration.toMinutesPart() + "mm : " + duration.toSecondsPart() + "ss.";
            }
        }
        return "N/A";
    }

    private String calculateReleaseTime(List<CloudEvent> eventsForModule, String pipelineId) {
        if (eventsForModule != null) {
            OffsetDateTime startTime = null;
            OffsetDateTime releaseTime = null;
            for (CloudEvent ce : eventsForModule) {
                if (ce.getType().equals("CDF.Pipeline.Started")) {

                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        startTime = ce.getTime();
                    }

                }
                if (ce.getType().equals("CDF.Artifact.Released")) {
                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        releaseTime = ce.getTime();
                    }
                }
            }
            if (startTime != null && releaseTime != null) {
                Duration duration = Duration.between(startTime, releaseTime);

                return duration.toHoursPart() + "hh : " + duration.toMinutesPart() + "mm : " + duration.toSecondsPart() + "ss.";
            }
        }
        return "N/A";
    }

    private String calculatePipelineTime(List<CloudEvent> eventsForModule, String pipelineId) {
        if (eventsForModule != null) {
            OffsetDateTime startTime = null;
            OffsetDateTime pipelineTime = null;
            for (CloudEvent ce : eventsForModule) {
                if (ce.getType().equals("CDF.Pipeline.Started")) {

                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        startTime = ce.getTime();
                    }

                }
                if (ce.getType().equals("CDF.Pipeline.Finished")) {
                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        pipelineTime = ce.getTime();
                    }
                }
            }
            if (startTime != null && pipelineTime != null) {
                Duration duration = Duration.between(startTime, pipelineTime);

                return duration.toHoursPart() + "hh : " + duration.toMinutesPart() + "mm : " + duration.toSecondsPart() + "ss.";
            }
        }
        return "N/A";
    }

    private String calculateTestsTime(List<CloudEvent> eventsForModule, String pipelineId) {
        if (eventsForModule != null) {
            OffsetDateTime startTime = null;
            OffsetDateTime testsTime = null;
            for (CloudEvent ce : eventsForModule) {
                if (ce.getType().equals("CDF.Artifact.TestsStarted")) {

                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        startTime = ce.getTime();
                    }

                }
                if (ce.getType().equals("CDF.Artifact.TestsEnded")) {
                    String cdfpipeid = ce.getExtension("cdfpipeid").toString();
                    if (pipelineId.equals(cdfpipeid)) {
                        testsTime = ce.getTime();
                    }
                }
            }
            if (startTime != null && testsTime != null) {
                Duration duration = Duration.between(startTime, testsTime);

                return duration.toHoursPart() + "hh : " + duration.toMinutesPart() + "mm : " + duration.toSecondsPart() + "ss.";
            }
        }
        return "N/A";
    }
}
