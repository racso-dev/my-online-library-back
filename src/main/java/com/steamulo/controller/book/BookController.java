package com.steamulo.controller.book;

import com.steamulo.controller.user.UserController;
import com.steamulo.services.book.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAuthority('BOOK_GET')")
    @GetMapping()
    public @ResponseBody Optional getBooks(@RequestParam("category") String category) {
        return bookService.getBooks(category);
    }
}
