package book.providers;

import convertors.BookConverter;
import entity.Book;
import http.client.HttpsClient;

import java.net.URL;
import java.util.Optional;

public class RemoteBookProvider implements BookProvider {

    private HttpsClient client;
    private BookConverter converter;

    public RemoteBookProvider(HttpsClient client, BookConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    @Override
    public Optional<Book> getBook(URL url) throws Exception {
        return Optional.ofNullable(converter.convertHtmlToBook(client.getStringHtmlContentOfUrl(url)));
    }
}