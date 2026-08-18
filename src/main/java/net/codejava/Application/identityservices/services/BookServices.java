package net.codejava.Application.identityservices.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.Application.identityservices.dto.request.BookCreationRequest;
import net.codejava.Application.identityservices.entity.Book;
import net.codejava.Application.identityservices.repository.BookRepository;

@Service
public class BookServices {

    @Autowired
    private BookRepository bookRepository;

    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createRequest(BookCreationRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        
        return bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }


}
