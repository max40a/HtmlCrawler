package com.education.cherkassy.max40a.core.book;

import com.education.cherkassy.max40a.core.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public int saveBook(@RequestBody String jsonBook) throws UnknownHostException {
        return this.repository.saveJsonBook(jsonBook);
    }

    @GetMapping()
    public List<Book> getAllBooks(@RequestParam Integer page) throws UnknownHostException {
        System.out.println("page = " + page);
        return this.repository.getAllBooks(page);
    }

    @GetMapping("/categories")
    public Set<String> getAllCategories() throws UnknownHostException {
        return this.repository.getAllCategories();
    }

    @GetMapping("/search/category")
    public List<Book> getBooksByCategory(@RequestParam String category) throws UnknownHostException {
        return this.repository.getBooksByCategories(category);
    }

    @GetMapping("/search/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) throws UnknownHostException {
        return this.repository.getBooksByAuthor(author);
    }

    @GetMapping("/authors")
    public Set<String> getAllAuthors() throws UnknownHostException {
        return this.repository.getAllAuthors();
    }
}