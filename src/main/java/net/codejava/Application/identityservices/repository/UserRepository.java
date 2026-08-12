package net.codejava.Application.identityservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.Application.identityservices.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
}
