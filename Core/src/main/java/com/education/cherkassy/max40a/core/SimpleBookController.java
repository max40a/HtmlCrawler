package com.education.cherkassy.max40a.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

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
        return repository.saveJsonBook(jsonBook);
    }
}