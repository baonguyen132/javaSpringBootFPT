package net.codejava.Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.Application.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
