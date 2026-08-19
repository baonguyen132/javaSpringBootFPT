package net.codejava.Application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.codejava.Application.dto.request.BookCreationRequest;
import net.codejava.Application.entity.Book;
import net.codejava.Application.services.BookServices;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;



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
