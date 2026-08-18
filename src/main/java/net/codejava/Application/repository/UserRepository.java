package net.codejava.Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.Application.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
