package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

public class DescriptionFinder implements PropertyFinder<String> {

    private String cssQuery = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select(cssQuery);
            if (select.isEmpty()) return Optional.empty();
            Elements next = select.parents().next();
            if (next.isEmpty()) return Optional.empty();
            Element first = next.first();
            return !first.hasText() ? Optional.empty() : Optional.ofNullable(first.text());
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}