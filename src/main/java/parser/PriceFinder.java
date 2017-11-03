package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class PriceFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) {
        Elements select = document.select("div > span.fn-price");
        return select.isEmpty() ? Optional.empty() : Optional.ofNullable(select.first().text());
    }
}