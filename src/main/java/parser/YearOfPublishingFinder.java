package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class YearOfPublishingFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Рік видання)";

    @Override
    public Optional<String> findProperty(Document document) {
        String publishing = document.select(cssQuery).next().text();
        return (publishing.isEmpty()) ? Optional.empty() : Optional.of(publishing);
    }
}