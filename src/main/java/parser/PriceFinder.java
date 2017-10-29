package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class PriceFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) {
        String price = document.select("div > span.fn-price").first().text();
        return (price.isEmpty()) ? Optional.empty() : Optional.ofNullable(price);
    }
}