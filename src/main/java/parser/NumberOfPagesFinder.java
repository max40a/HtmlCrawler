package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class NumberOfPagesFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Кількість сторінок)";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select(cssQuery);
            return (select.isEmpty()) ? Optional.empty() : Optional.ofNullable(select.next().text());
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}