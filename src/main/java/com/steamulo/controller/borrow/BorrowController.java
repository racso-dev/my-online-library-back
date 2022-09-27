package com.steamulo.controller.borrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.steamulo.dto.BookDto;
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

    /**
     * WS de récupération des livres empruntés par l'utilisateur connecté
     * 
     * @return une liste de livres
     */
    @GetMapping(value = "/self")
    public @ResponseBody List<BookDto> getUserBorrowedBooks() {
        return borrowService
                .getBorrowedBooksByUserId(authService.getAuthUser().getId());
    }

    /**
     * WS de création d'un emprunt de livre
     *
     * @param bookId l'identifiant du livre
     * @return une 204 no content si l'emprunt a été créé, une erreur 400 sinon
     */
    @PostMapping()
    public @ResponseBody Optional borrowBook(@RequestParam("bookId") String bookId) {
        return borrowService.createBorrow(authService.getAuthUser(), bookId);
    }
}
