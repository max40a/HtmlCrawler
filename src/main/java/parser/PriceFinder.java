package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class PriceFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) {
        Elements select = document.select("div > span.fn-price");
        if (select.isEmpty()) return Optional.empty();
        return Optional.ofNullable(select.first().text());
    }
}
