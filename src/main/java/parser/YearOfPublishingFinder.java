package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class YearOfPublishingFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Рік видання)";

    @Override
    public Optional<String> findProperty(Document document) {
        Elements select = document.select(cssQuery);
        return (select.isEmpty()) ? Optional.empty() : Optional.ofNullable(select.next().text());
    }
}