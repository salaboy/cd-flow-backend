package com.salaboy.cdf;

import com.salaboy.cdf.model.entities.build.Module;
import com.salaboy.cdf.model.entities.build.PipelineRun;
import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.model.entities.run.Environment;
import com.salaboy.cdf.model.entities.run.Service;
import com.salaboy.cdf.model.metrics.*;
import com.salaboy.cdf.services.EventStoreService;
import com.salaboy.cdf.services.BuildtimeService;
import com.salaboy.cdf.services.RuntimeService;
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
    private BuildtimeService buildTimeService;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("environments")
    public EnvironmentsMetrics getEnvironmentMetrics() {
        EnvironmentsMetrics environmentsMetrics = new EnvironmentsMetrics();
        Iterable<Environment> environments = runtimeService.getEnvironments();
        for(Environment e : environments){
            EnvironmentMetrics environmentMetrics = new EnvironmentMetrics();
            environmentMetrics.setEnvironmentName(e.getName());
            for(Service service : e.getServices()){
                ServiceMetrics sm = new ServiceMetrics();
                sm.setServiceName(service.getName());
            }
            environmentsMetrics.addEnvironmentMetrics(environmentMetrics);
        }

        return environmentsMetrics;

    }


    @GetMapping("projects")
    public ProjectsMetrics getProjectMetrics() {
        ProjectsMetrics projectsMetrics = new ProjectsMetrics();

        Iterable<Project> projects = buildTimeService.getProjects();
        for (Project p : projects) {
            ProjectMetrics projectMetrics = new ProjectMetrics(p.getName());
            for (Module module : p.getModules()) {
                ModuleMetrics moduleMetrics = new ModuleMetrics(p.getName(), module.getName(), module.getRepoUrl());
                Set<PipelineRun> pipelineRuns = module.getPipelineRuns();
                List<CloudEvent> eventsForModule = eventStoreService.getEventsForModule(module.getName());
                for (PipelineRun pr : pipelineRuns) {
                    PipelineMetrics pipelineMetrics = new PipelineMetrics(pr.getPipelineId());
                    String buildTime = calculateBuildTime(eventsForModule, pr.getPipelineId());
                    pipelineMetrics.setBuildTime(buildTime);
                    String testTime = calculateTestsTime(eventsForModule, pr.getPipelineId());
                    pipelineMetrics.setTestsTime(testTime);
                    String releaseTime = calculateReleaseTime(eventsForModule, pr.getPipelineId());
                    pipelineMetrics.setReleaseTime(releaseTime);
                    String pipelineTime = calculatePipelineTime(eventsForModule, pr.getPipelineId());
                    pipelineMetrics.setPipelineTime(pipelineTime);
                    if (pipelineTime == "N/A") {
                        pipelineMetrics.setPipelineStatus("STARTED");
                    } else {
                        pipelineMetrics.setPipelineStatus("FINISHED");
                    }
                    if (buildTime != "N/A") {
                        moduleMetrics.setModuleStatus("BUILT");
                    }
                    if (testTime != "N/A") {
                        moduleMetrics.setModuleStatus("TESTED");
                    }
                    if (releaseTime != "N/A") {
                        moduleMetrics.setModuleStatus("RELEASED");
                    }

                    moduleMetrics.addPipelineMetric(pipelineMetrics);
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
