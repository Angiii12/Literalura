package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(name = "birth")
    private Integer birthYear;
    @Column(name = "death")
    private Integer deathYear;
//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Book> book;

    public Author(){}
    public Author(AuthorData authorData) {
        try{
            this.birthYear = authorData.birthYear();
        } catch (NumberFormatException e){
            this.birthYear = 0;
        }

        try{
            this.deathYear = authorData.deathYear();
        } catch(NumberFormatException e){
            this.deathYear = 0;
        }

        this.name = authorData.name();
    }

    @Override
    public String toString() {
        return "Autor: " + name + "\n" +
                "Birth: " + birthYear + "\n" +
                "Death: " + deathYear + "\n";
    }
}
