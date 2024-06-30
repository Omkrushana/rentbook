package com.crio.rentread.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private boolean availability;
    private String author;
    private String genre;

    public Book(long id, String title, boolean availability, String author, String genre) {
        this.id = id;
        this.title = title;
        this.availability = availability;
        this.author = author;
        this.genre = genre;
    }

    public Book() {
        //TODO Auto-generated constructor stub
    }
}
