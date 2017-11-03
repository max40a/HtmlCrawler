package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class PriceFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select("div > span.fn-price");
            return select.isEmpty() ? Optional.empty() : Optional.ofNullable(select.first().text());
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}