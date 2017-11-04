package data.providers;

import convertors.BookConverter;
import entity.Book;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class LocalDataProvider implements DataProvider {

    private BookConverter converter;

    public LocalDataProvider(BookConverter converter) {
        this.converter = converter;
    }

    @Override
    public Book getListOfBooks(URL url) throws IOException, URISyntaxException {
        return converter.toBook(Jsoup.parse(Paths.get(url.toURI()).toFile(), StandardCharsets.UTF_8.name()).html());
    }
}