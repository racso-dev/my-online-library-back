package com.steamulo.services.book;

import io.prismic.*;
import io.prismic.Fragment.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.steamulo.persistence.entity.Borrow;
import com.steamulo.persistence.repository.BorrowRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BookService {

    private final BorrowRepository borrowRepository;

    public BookService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private HashMap<String, String> matches = new HashMap<>(
            Map.of(
                    "literature", "Littérature",
                    "comics", "Bande dessinée",
                    "utility", "Utilitaire",
                    "children", "Livre pour enfant"));

    public Optional<ArrayList<HashMap<String, String>>> getBooks(String category) {
        // Fetching Prismic API
        Response response = Api.get("https://my-bouquins.prismic.io/api/v1",
                "MC5ZeDlGX1JFQUFDSUFRcEN5.Be-_vRcaP1Yp77-977-9R--_ve-_ve-_ve-_vQZbfO-_vQRufEHvv71BGwnvv70fdkd777-9")
                .query(
                        Predicates.at("document.type", "books"),
                        Predicates.fulltext("my.books.category", matches.get(category)))
                .fetch("books.title", "books.sumup", "books.publication_date", "books.cover", "books.category")
                .submit();
        List<Document> documents = response.getResults();

        // Format documents to a list of books containing only the fields we need
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
        List<Borrow> borrowedBooks = borrowRepository.findAll();
        for (Borrow borrowedBook : borrowedBooks) {
            for (HashMap<String, String> book : books) {
                if (book.get("id").equals(borrowedBook.getBookId())) {
                    books.remove(book);
                    break;
                }
            }
        }
        return Optional.of(books);
    }
}
