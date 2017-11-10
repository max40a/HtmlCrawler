package parser.alterntive;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parser.PropertyFinder;
import parser.PropertyNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AuthorsFinder2 implements PropertyFinder<List<String>> {

    private String selectStr = "div#features tr.params > td.attr:matchesOwn(^Автор$)";

    @Override
    public Optional<List<String>> findProperty(Document document) {
        try {
            Elements select = document.select(selectStr);
            if (select.isEmpty()) return Optional.empty();
            Elements searchElement = select.next();
            if (searchElement.isEmpty()) return Optional.empty();
            String authors = searchElement.text();
            return Optional.of(Collections.singletonList(authors));
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}