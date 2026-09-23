package net.codejava.Application.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import net.codejava.Application.repository.ProjectRepository ;
import net.codejava.Application.entity.Project;

@Service 
public class ProjectServices {
    @Autowired
    private final ProjectRepository projectRepository ;

    public ProjectServices(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository ;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project) ;
    }
}
