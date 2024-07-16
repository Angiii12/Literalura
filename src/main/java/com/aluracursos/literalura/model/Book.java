package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private String author;
    private String languages;
    private Integer downloadCount;

    public Book(){}

    public Book(ResultData bookData){
        this.title = bookData.title();
        this.author = bookData.authors().get(0).name();
        this.languages = bookData.languages().get(0);
        this.downloadCount = bookData.downloads();
    }


    @Override
    public String toString() {
        return "********************************\n"+
                "-- "+ title.toUpperCase() + " --\n" +
                "Author: " + author + "\n" +
                "Download count: " + downloadCount + "\n"+
                "Language: " + languages + "\n" +
                "*******************************";
    }

}
