package net.codejava.Application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
    private String name ;
    private String author ;
    private String publisher ;

    public String getAuthor() {
        return author;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
}
