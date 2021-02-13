package com.salaboy.cdf;

import com.salaboy.cdf.model.entities.build.Project;
import com.salaboy.cdf.services.BuildtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/environments/")
public class EnvironmentsController {

    @Autowired
    private BuildtimeService buildTimeService;

    @GetMapping("")
    public Iterable<Project> getProjects() {
        return buildTimeService.getProjects();
    }

}
