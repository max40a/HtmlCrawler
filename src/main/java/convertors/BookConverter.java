package convertors;

import entity.Book;
import entity.Isbn;
import org.jsoup.nodes.Document;
import parser.PropertyFinder;

import java.util.*;

public class BookConverter {

    private Map<String, PropertyFinder> mapOfSearchEngines;
    private Map<String, String> languageMap = new HashMap<>();

    //TODO fixme
    public BookConverter(Map<String, PropertyFinder> map) {
        this.mapOfSearchEngines = map;

        languageMap.put("Українська", "UA");
        languageMap.put("Російська", "RU");
        languageMap.put("Англійська", "EN");
    }

    //TODO fixme
    @SuppressWarnings("unchecked")
    public Book toBook(Document document) {
        Book book = new Book();

        this.mapOfSearchEngines.get("title").findProperty(document).ifPresent(s -> book.setTitle((String) s));
        this.mapOfSearchEngines.get("authors").findProperty(document).ifPresent(sl -> book.setAuthors((List<String>) sl));
        this.mapOfSearchEngines.get("publishing").findProperty(document).ifPresent(s -> book.setPublishing((String) s));
        this.mapOfSearchEngines.get("yearOfPublishing").findProperty(document).ifPresent(s -> book.setYearOfPublishing((String) s));
        this.mapOfSearchEngines.get("numberOfPages").findProperty(document).ifPresent(s -> {
            book.setNumberOfPages(Short.valueOf((String) s));
        });
        this.mapOfSearchEngines.get("ISBN").findProperty(document).ifPresent(ls -> {
            book.setIsbnList(Collections.singletonList(toIsbn((List<String>) ls)));
        });
        this.mapOfSearchEngines.get("price").findProperty(document).ifPresent(s -> book.setPrice(Double.valueOf((String) s)));
        this.mapOfSearchEngines.get("categories").findProperty(document).ifPresent(ls -> {
            book.setCategories((List<String>) ls);
        });
        this.mapOfSearchEngines.get("description").findProperty(document).ifPresent(s -> book.setDescription((String) s));


        return book;
    }

    private Isbn toIsbn(List<String> isbnAttrs) {
        Isbn isbn = new Isbn();

        String number = isbnNumberFormatter(isbnAttrs.get(0));
        String language = languageDetect(isbnAttrs.get(1));
        String type = String.valueOf(number.length());

        isbn.setLanguage(language);
        isbn.setNumber(number);
        isbn.setType(type);
        isbn.setTranslation(true);

        return isbn;
    }

    private String languageDetect(String language) {
        return languageMap.getOrDefault(language, "NOT LANGUAGE FOUNDED");
    }

    private String isbnNumberFormatter(String inputIsbn) {
        return String.join("", inputIsbn.split("-"));
    }
}
