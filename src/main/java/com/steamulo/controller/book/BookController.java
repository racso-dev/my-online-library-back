package com.steamulo.controller.book;

import com.steamulo.controller.user.UserController;
import com.steamulo.services.book.BookService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAuthority('BOOK_GET')")
    @GetMapping()
    public @ResponseBody Optional getBooks(@RequestParam("category") String category) {
        log.info("Getting books for category {}", category);
        return bookService.getBooks(category);
    }
}
