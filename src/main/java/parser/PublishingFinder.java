package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class PublishingFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Видавництво)";

    @Override
    public Optional<String> findProperty(Document document) throws PropertyNotFoundException {
        try {
            String publishing = document.select(cssQuery).next().text();
            return (publishing.isEmpty()) ? Optional.empty() : Optional.of(publishing);
        } catch (Exception e) {
            String message = String.format("\"PUBLISHING\" Not Found for URL: %s, cause: %s", document.location(), e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}
