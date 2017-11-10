package parser.alterntive;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parser.PropertyFinder;
import parser.PropertyNotFoundException;

import java.util.Optional;

public class DescriptionFinder2 implements PropertyFinder<String> {

    private String query = "div.tab-content > div#annotation > p";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select(query);
            if (select.isEmpty()) return Optional.empty();
            return Optional.ofNullable(select.text());
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}