package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;

public class DescriptionFinder implements PropertyFinder<String> {

    private String cssQuery = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";

    @Override
    public Optional<String> findProperty(Document document) {
        Element description = document.select(cssQuery).parents().next().first();
        if(!description.hasText()) {
            return Optional.empty();
        }
        return Optional.of(description.text());
    }
}