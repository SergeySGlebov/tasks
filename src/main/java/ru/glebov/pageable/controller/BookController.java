package ru.glebov.pageable.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glebov.pageable.model.Author;
import ru.glebov.pageable.model.Book;
import ru.glebov.pageable.repository.AuthorRepository;
import ru.glebov.pageable.repository.BookRepository;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable int id) {
        return bookRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, getSort(sort));
        Page<Book> books = bookRepository.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book) {
        if (book.getAuthor() == null) {
            return ResponseEntity.badRequest().build();
        }

        book.setAuthor(getAuthor(book));
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.created(URI.create("/api/books/" + savedBook.getId()))
                .body(savedBook);

    }

    private Author getAuthor(@RequestBody Book book) {
        if (book.getAuthor().getId() == null) {
            return authorRepository.save(book.getAuthor());
        } else {
            return authorRepository.findById(book.getAuthor().getId())
                    .orElseGet(() -> authorRepository.save(book.getAuthor()));
        }
    }

    @PutMapping
    public ResponseEntity<Book> update(@RequestBody Book book) {
        if (book.getAuthor() == null) {
            return ResponseEntity.badRequest().build();
        }
        return bookRepository.findById(book.getId())
                .map(findBook -> {
                    book.setAuthor(getAuthor(book));
                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok(updatedBook);
                }).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (bookRepository.delete(id) > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Sort getSort(String[] sort) {
        if (sort.length >= 2) {
            return Sort.by(new Sort.Order(
                    Sort.Direction.fromString(sort[1]),
                    sort[0]));
        }
        return Sort.by(sort[0]);
    }

}
