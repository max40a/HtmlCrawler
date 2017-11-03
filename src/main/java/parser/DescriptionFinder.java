package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class DescriptionFinder implements PropertyFinder<String> {

    private String cssQuery = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            String description = document.select(cssQuery).parents().next().first().text();
            return description.isEmpty() ? Optional.empty() : Optional.of(description);
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}
