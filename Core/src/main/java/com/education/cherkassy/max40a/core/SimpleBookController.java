package com.education.cherkassy.max40a.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class SimpleBookController {

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void getBook(@RequestBody String book) {
        System.out.println(book);
    }
}