package net.codejava.Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.codejava.Application.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
