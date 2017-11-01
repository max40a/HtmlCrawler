package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class PriceFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) throws PropertyNotFoundException {
        try {
            String price = document.select("div > span.fn-price").first().text();
            return (price.isEmpty()) ? Optional.empty() : Optional.of(price);
        } catch (Exception e) {
            String message = String.format("\"PRICE\" Not Found for URL: %s, cause: %s", document.location(), e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}
