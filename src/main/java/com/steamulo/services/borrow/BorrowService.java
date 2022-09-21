package com.steamulo.services.borrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.Borrow;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.BorrowRepository;

import io.prismic.*;
import io.prismic.Fragment.Image;

@Component
public class BorrowService {

    private final BorrowRepository borrowRepository;

    public BorrowService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
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

    public Optional<ArrayList<HashMap<String, String>>> getBorrowedBooksByUserId(Long userId) {
        List<Borrow> borrowed = borrowRepository.findByUserId(userId).orElse(new ArrayList<>());
        List<String> bookIds = borrowed.stream().map(Borrow::getBookId).collect(Collectors.toList());
        Response response = Api.get("https://my-bouquins.prismic.io/api/v1",
                "MC5ZeDlGX1JFQUFDSUFRcEN5.Be-_vRcaP1Yp77-977-9R--_ve-_ve-_ve-_vQZbfO-_vQRufEHvv71BGwnvv70fdkd777-9")
                .getByIDs(bookIds).submit();
        List<Document> documents = response.getResults();
        ArrayList<HashMap<String, String>> books = new ArrayList<>();
        for (Document document : documents) {
            HashMap<String, String> book = new HashMap<>();
            String title = document.getText("books.title");
            String sumup = document.getText("books.sumup");
            String publicationDate = document.getDate("books.publication_date").getValue().toString();
            Image cover = document.getImage("books.cover");
            String categoryText = document.getText("books.category");
            book.put("title", title);
            book.put("sumup", sumup);
            book.put("publicationYear", publicationDate.split("-")[0]);
            book.put("cover", cover.getUrl());
            book.put("category", categoryText);
            book.put("id", document.getId());
            books.add(book);
        }
        return Optional.of(books);
    }
}
