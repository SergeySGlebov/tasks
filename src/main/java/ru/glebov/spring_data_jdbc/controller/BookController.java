package ru.glebov.spring_data_jdbc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.glebov.spring_data_jdbc.Repository.BookRepository;
import ru.glebov.spring_data_jdbc.model.Book;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable int id) {
        return bookRepository.get(id);
    }

    @PostMapping
    public Book save(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Book book) {
        bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        bookRepository.delete(id);
    }

}
