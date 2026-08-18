package net.codejava.Application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.codejava.Application.identityservices.dto.request.BookCreationRequest;
import net.codejava.Application.identityservices.entity.Book;
import net.codejava.Application.identityservices.services.BookServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/books")
public class BookController {
    private BookServices bookServices;

    public BookController(BookServices bookServices) {
        this.bookServices = bookServices;
    }

    @PostMapping("")
    public Book createBook(@RequestBody BookCreationRequest request) {
        return bookServices.createRequest(request);
    }
    @GetMapping("")
    public List<Book> getBooks() {
        return bookServices.getBooks();
    }
    
}
