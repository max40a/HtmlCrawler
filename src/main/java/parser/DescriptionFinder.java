package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class DescriptionFinder implements PropertyFinder<String> {

    private String cssQuery = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";

    @Override
    public Optional<String> findProperty(Document document) {
        String description = document.select(cssQuery).parents().next().first().text();
        return description.isEmpty() ? Optional.empty() : Optional.of(description);
    }
}