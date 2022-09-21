package com.steamulo.services.borrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.steamulo.dto.BookDto;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.Borrow;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.BorrowRepository;
import com.steamulo.services.book.BookService;

import io.prismic.*;
import io.prismic.Fragment.Image;

@Component
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookService bookService;

    public BorrowService(BorrowRepository borrowRepository, BookService bookService) {
        this.borrowRepository = borrowRepository;
        this.bookService = bookService;
    }

    public Optional<Borrow> createBorrow(User user, String bookId) {
        Optional<Borrow> match = borrowRepository.findByBookId(bookId);
        if (match.isPresent()) {
            return Optional.empty();
        }

        System.out.println(
                "Borrowing book " + bookId + " for user " + user.getId() + " user activation " + user.getActivated());
        if (Boolean.FALSE.equals(user.getActivated())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "User not authorized to borrow books");
        }

        Borrow borrow = Borrow.builder().bookId(bookId).userId(user.getId()).build();

        return Optional.of(borrowRepository.save(borrow));
    }

    public List<BookDto> getBorrowedBooksByUserId(Long userId) {
        List<Borrow> borrowed = borrowRepository.findByUserId(userId).orElse(new ArrayList<>());
        List<String> bookIds = borrowed.stream().map(Borrow::getBookId).collect(Collectors.toList());
        return bookService.fetchBooksByIds(bookIds);
    }
}
