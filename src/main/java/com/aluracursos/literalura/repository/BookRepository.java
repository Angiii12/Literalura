package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.languages = :lan")
    List<Book> findByLanguages(@Param("lan") String lan);
}
