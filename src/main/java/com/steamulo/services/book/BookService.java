package com.steamulo.services.book;

import io.prismic.*;
import lombok.extern.slf4j.Slf4j;

import com.steamulo.conf.properties.PrismicProperties;
import com.steamulo.dto.BookDto;
import com.steamulo.enums.Category;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.Borrow;
import com.steamulo.persistence.repository.BorrowRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class BookService {

    private final BorrowRepository borrowRepository;
    private final PrismicProperties prismicProperties;

    public BookService(BorrowRepository borrowRepository, PrismicProperties prismicProperties) {
        this.borrowRepository = borrowRepository;
        this.prismicProperties = prismicProperties;
    }

    private List<BookDto> formatBooks(List<Document> documents) {
        ArrayList<BookDto> books = new ArrayList<>();
        for (Document document : documents) {
            BookDto book = new BookDto(
                    document.getText("books.title"),
                    document.getText("books.sumup"),
                    document.getDate("books.publication_date").getValue().toString().split("-")[0],
                    document.getText("books.category"),
                    document.getImage("books.cover").getUrl(),
                    document.getId());
            books.add(book);
        }
        return books;
    }

    public List<BookDto> fetchBooksByIds(List<String> ids) {
        Response response = Api.get(prismicProperties.getUrl(), prismicProperties.getAccessToken())
                .getByIDs(ids).submit();
        List<Document> documents = response.getResults();

        return formatBooks(documents);
    }

    public List<BookDto> fetchBooksByCategory(String category) {
        log.info("Fetching books by category: " + category);
        Response response = Api.get(prismicProperties.getUrl(), prismicProperties.getAccessToken())
                .query(
                        Predicates.at("document.type", "books"),
                        Predicates.fulltext("my.books.category",
                                Arrays.asList(Category.values()).stream()
                                        .filter(cat -> cat.getEndpoint().equals(category)).findFirst()
                                        .orElseThrow(
                                                () -> new ApiException(HttpStatus.BAD_REQUEST, "Category not found"))
                                        .getCategoryName()))
                .fetch("books.title", "books.sumup", "books.publication_date", "books.cover", "books.category")
                .submit();
        List<Document> documents = response.getResults();
        return formatBooks(documents);
    }

    public List<BookDto> getBooks(String category) {
        List<BookDto> books = fetchBooksByCategory(category);
        List<Borrow> borrowedBooks = borrowRepository.findAll();
        for (Borrow borrowedBook : borrowedBooks) {
            for (BookDto book : books) {
                if (book.getId().equals(borrowedBook.getBookId())) {
                    books.remove(book);
                    break;
                }
            }
        }
        return books;
    }
}
