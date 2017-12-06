package com.education.cherkassy.max40a.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

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
    public List<String> getAllBooks() throws UnknownHostException {
        return this.repository.getAllBooks();
    }
}