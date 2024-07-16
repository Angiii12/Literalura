package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Author;
import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.model.BookData;
import com.aluracursos.literalura.model.ResultData;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.service.APIConsumption;
import com.aluracursos.literalura.service.DataConversor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private APIConsumption api = new APIConsumption();
    private final String URL_BASE = "https://gutendex.com/books/";
    private DataConversor conversor = new DataConversor();
    private List<Book> books;
    private List<Book> booksLan;
    private List<Author> authors;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void menu() {
        var option = -1;
        while (option != 0) {
            var m = """
                    1 - Search book by name 
                    2 - List registered books
                    3 - List registered authors
                    4 - List authors alive in a given year
                    5 - List books by language
                    0 - Exit
                    """;
            System.out.println(m);
            option = keyboard.nextInt();
            keyboard.nextLine();
            switch (option) {
                case 1:
                    searchBookByName();
                    break;
                case 2:
                    ListRegisteredBooks();
                    break;
                case 3:
                    ListRegisteredAuthors();
                    break;
                case 4:
                    SearchAuthorsAliveByDate();
                    break;
                case 5:
                    ListBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Closing...");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void ListBooksByLanguage() {
        System.out.println("Insert the first 2 characters of the language: ");
        var lan = keyboard.nextLine();
        try{
            booksLan = bookRepository.findByLanguages(lan);
            if(booksLan.isEmpty()) {
                System.out.println("No Books Found");
            }
            booksLan.forEach(System.out::println);
        } catch (RuntimeException e){
            System.out.println("Invalid language");
        }

    }

    private void SearchAuthorsAliveByDate() {
        System.out.println("Insert year: ");
        var in = Integer.parseInt(keyboard.nextLine());
        try{
            List<Author> authors = authorRepository.findAuthorsByDeathYear(in);
            if(authors.isEmpty()) {
                System.out.println("No authors found");
            }
            authors.forEach(System.out::println);
        }catch (RuntimeException e){
            System.out.println("Invalid year");
        }
    }

    private void ListRegisteredBooks() {
        books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No books found");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void ListRegisteredAuthors() {
        authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("No authors found");
        } else {
            authors.forEach(System.out::println);
        }
    }

    private BookData getBook() {
        System.out.println("Insert book name: ");
        var name = keyboard.nextLine();
        var json = api.obtainData(URL_BASE + "?search=" + name.replace(" ", "%20").toLowerCase());
        BookData book = conversor.obtainData(json, BookData.class);
        return book;
    }
    private void searchBookByName() {
        boolean bookFound = false;
        do {
            try {
                BookData bookData = getBook();

                if (bookData.results().isEmpty()) {
                    throw new RuntimeException("Book not found");
                }

                ResultData firstResult = bookData.results().get(0);
                List<Book> existingBooks = bookRepository.findByTitle(firstResult.title());

                if (!existingBooks.isEmpty()) {
                    throw new RuntimeException("Book already in the data base");
                }

                Book book = new Book(firstResult);
                Author author = new Author(firstResult.authors().get(0));

                System.out.println(book);
                bookRepository.save(book);
                authorRepository.save(author);

                bookFound = true;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        } while (!bookFound);
    }
}
