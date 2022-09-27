package com.steamulo.controller.book;

import com.steamulo.dto.BookDto;
import com.steamulo.services.book.BookService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * WS de récupération des livres disponibles d'une catégorie
     *
     * @param category la catégorie de livre à récupérer
     * @return une liste de livre
     */
    @PreAuthorize("hasAuthority('BOOK_GET')")
    @GetMapping()
    public @ResponseBody List<BookDto> getBooks(@RequestParam("category") String category) {
        log.info("Getting books for category {}", category);
        return bookService.getBooks(category);
    }
}
