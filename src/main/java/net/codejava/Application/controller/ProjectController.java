package net.codejava.Application.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping
    public String getMethodName() {
        return "hello";
    }

}
