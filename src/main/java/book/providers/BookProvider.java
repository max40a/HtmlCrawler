package book.providers;

import entity.Book;

import java.net.URL;
import java.util.Optional;

public interface BookProvider {
    Optional<Book> getBook(URL url) throws Exception;
}