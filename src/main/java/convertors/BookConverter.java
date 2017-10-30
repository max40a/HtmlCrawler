package convertors;

import entity.Book;
import entity.Isbn;
import org.jsoup.nodes.Document;
import parser.PropertyFinder;

import java.util.List;
import java.util.Map;

public class BookConverter {

    private Map<String, PropertyFinder> mapOfSearchEngines;

    public BookConverter(Map<String, PropertyFinder> map) {
        this.mapOfSearchEngines = map;
    }

    //TODO fixme
    @SuppressWarnings("unchecked")
    public Book toBook(Document document) {
        return Book
                .builder()
                .title((String) this.mapOfSearchEngines.get("title").findProperty(document).get())
                .authors((List<String>) this.mapOfSearchEngines.get("authors").findProperty(document).get())
                .publishing((String) mapOfSearchEngines.get("publishing").findProperty(document).get())
                .yearOfPublishing((String) this.mapOfSearchEngines.get("yearOfPublishing").findProperty(document).get())
                .numberOfPages(Short.valueOf((String) this.mapOfSearchEngines.get("numberOfPages").findProperty(document).get()))
                .isbn((Isbn) this.mapOfSearchEngines.get("ISBN").findProperty(document).get())
                .price(Double.valueOf((String) this.mapOfSearchEngines.get("price").findProperty(document).get()))
                .categories((List<String>) this.mapOfSearchEngines.get("categories").findProperty(document).get())
                .description((String) this.mapOfSearchEngines.get("description").findProperty(document).get())
                .build();
    }
}
