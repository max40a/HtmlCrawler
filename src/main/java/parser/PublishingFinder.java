package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class PublishingFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Видавництво)";

    @Override
    public Optional<String> findProperty(Document document) {
        String publishing = document.select(cssQuery).next().text();
        return (publishing.isEmpty()) ? Optional.empty() : Optional.of(publishing);
    }
}