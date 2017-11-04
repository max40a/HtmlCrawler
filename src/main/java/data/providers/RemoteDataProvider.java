package data.providers;

import convertors.BookConverter;
import entity.Book;
import http.client.HttpsClient;

import java.net.URL;
import java.nio.file.Path;

public class RemoteDataProvider implements DataProvider {
    private HttpsClient client;
    private BookConverter converter;

    public RemoteDataProvider(HttpsClient client, BookConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    //@Override
    public Book getListOfBooks(Path pathTo) throws Exception {
        String html = client.getStringHtmlContentOfUrl(pathTo.toUri().toURL());
        return converter.toBook(html);
    }

    @Override
    public Book getListOfBooks(URL url) throws Exception {
        String html = client.getStringHtmlContentOfUrl(url);
        return converter.toBook(html);
    }

}
