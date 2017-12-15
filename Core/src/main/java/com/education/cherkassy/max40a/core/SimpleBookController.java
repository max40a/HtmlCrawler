package com.education.cherkassy.max40a.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class SimpleBookController {

    private BookRepository repository;

    @Autowired
    public SimpleBookController(BookRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public int saveBook(@RequestBody String jsonBook) throws UnknownHostException {
        return this.repository.saveJsonBook(jsonBook);
    }

    @GetMapping
    public List<Map<String, Object>> getAllBooks() throws UnknownHostException {
        return this.repository.getAllBooks();
    }

    @GetMapping("/categories")
    public Set<String> getAllCategories() throws UnknownHostException {
        return this.repository.getAllCategories();
    }

    @GetMapping("/search/category")
    public List<Map<String, Object>> getBooksByCategory(@RequestParam String category) throws UnknownHostException {
        return this.repository.getBooksByCategories(category);
    }

    @GetMapping("/search/author")
    public List<Map<String, Object>> getBooksByAuthor(@RequestParam String author) throws UnknownHostException {
        return this.repository.getBooksByAuthor(author);
    }

    @GetMapping("/authors")
    public Set<String> getAllAuthors() throws UnknownHostException {
        return this.repository.getAllAuthors();
    }
}