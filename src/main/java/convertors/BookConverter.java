package convertors;

import entity.Book;
import entity.Isbn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parser.PropertyFinder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BookConverter {

    private Map<String, PropertyFinder> mapOfSearchEngines;

    public BookConverter(Map<String, PropertyFinder> map) {
        this.mapOfSearchEngines = map;
    }

    @SuppressWarnings("unchecked")
    public Book convertHtmlToBook(String html) {
        Document document = Jsoup.parse(html, StandardCharsets.UTF_8.name());
        Book book = new Book();

        mapOfSearchEngines.get("title").findProperty(document).ifPresent(e -> book.setTitle((String) e));
        mapOfSearchEngines.get("authors").findProperty(document).ifPresent(e -> book.setAuthors((List<String>) e));
        mapOfSearchEngines.get("publishing").findProperty(document).ifPresent(e -> book.setPublishing((String) e));
        mapOfSearchEngines.get("yearOfPublishing").findProperty(document).ifPresent(e -> book.setPublishing((String) e));
        mapOfSearchEngines.get("numberOfPages").findProperty(document).ifPresent(e -> book.setNumberOfPages((String) e));
        mapOfSearchEngines.get("Isbn").findProperty(document).ifPresent(e -> book.setIsbn((Isbn) e));
        mapOfSearchEngines.get("categories").findProperty(document).ifPresent(e -> book.setCategories((List<String>) e));
        mapOfSearchEngines.get("description").findProperty(document).ifPresent(e -> book.setDescription((String) e));
        mapOfSearchEngines.get("price").findProperty(document).ifPresent(e -> book.setPrice((String) e));

        return book;
    }
}
