package db.book;

import book.providers.BookProvider;
import com.google.gson.Gson;
import convertors.AbstractBookParser;
import db.url.UrlsSupplier;
import entity.Book;
import validate.BookValidator;

import java.net.URL;
import java.sql.SQLException;

public class BookService {

    private UrlsSupplier urlsSupplier;
    private BookProvider bookProvider;
    private AbstractBookParser parser;
    private Gson bookToJsonConverter;
    private BookKeeper bookKeeper;

    public BookService(UrlsSupplier urlsSupplier,
                       BookProvider bookProvider,
                       AbstractBookParser parser,
                       Gson bookToJsonConverter,
                       BookKeeper bookKeeper) {
        this.urlsSupplier = urlsSupplier;
        this.bookProvider = bookProvider;
        this.parser = parser;
        this.bookToJsonConverter = bookToJsonConverter;
        this.bookKeeper = bookKeeper;
    }

    public void executeService() {
        try {
            for (URL url : urlsSupplier.getUrls()) {
                bookProvider.getBookHtml(url).ifPresent(html -> {
                    Book book = parser.convertHtmlToBook(html);
                    String bookJson = bookToJsonConverter.toJson(book);
                    try {
                        if (BookValidator.validateJson(bookJson, Book.class)) {
                            bookKeeper.saveBook(url.toString(), bookJson);
                            urlsSupplier.changeRetryStatusSuccessCase(url.toString());
                        } else {
                            urlsSupplier.changeRetryStatusUnfortunateCase(url.toString());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}