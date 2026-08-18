package net.codejava.Application.identityservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.Application.identityservices.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
