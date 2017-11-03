package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class PublishingFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Видавництво)";

    @Override
    public Optional<String> findProperty(Document document) {
        Elements select = document.select(cssQuery);
        return (select.isEmpty()) ? Optional.empty() : Optional.ofNullable(select.next().text());
    }
}