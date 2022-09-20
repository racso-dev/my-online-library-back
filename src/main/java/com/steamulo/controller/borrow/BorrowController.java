package com.steamulo.controller.borrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.steamulo.services.auth.AuthService;
import com.steamulo.services.borrow.BorrowService;

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private final AuthService authService;

    public BorrowController(BorrowService borrowService, AuthService authService) {
        this.borrowService = borrowService;
        this.authService = authService;
    }

    @GetMapping(value = "/self")
    public @ResponseBody Optional getUserBorrowedBooks() {
        Optional<ArrayList<HashMap<String, String>>> books = borrowService
                .getBorrowedBooksByUserId(authService.getAuthUser().getId());
        if (books.isPresent()) {
            return Optional.of(books.get());
        }
        return Optional.empty();
    }

    @PostMapping()
    public @ResponseBody Optional borrowBook(@RequestParam("bookId") String bookId) {
        return borrowService.createBorrow(authService.getAuthUser(), bookId);
    }
}
