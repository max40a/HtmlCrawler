package book.providers;

import convertors.BookConverter;
import entity.Book;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LocalBookProvider implements BookProvider {

    private BookConverter converter;

    public LocalBookProvider(BookConverter converter) {
        this.converter = converter;
    }

    @Override
    public Optional<Book> getBook(URL url) throws Exception {
        return Optional.ofNullable(converter.convertHtmlToBook(IOUtils.toString(url.toURI(), StandardCharsets.UTF_8.name())));
    }
}