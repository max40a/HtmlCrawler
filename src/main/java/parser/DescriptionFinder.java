package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

public class DescriptionFinder implements PropertyFinder<String> {

    private String cssQuery1 = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";
    private String cssQuery2 = "div.tab-content > div#annotation > p";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select(cssQuery1);
            if (!select.isEmpty()) {
                return firstSearch(select);
            } else {
                select = document.select(cssQuery2);
                return secondSelect(select);
            }
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }

    private Optional<String> firstSearch(Elements currentSelect) {
        Elements next = currentSelect.parents().next();
        if (next.isEmpty()) return Optional.empty();
        Element first = next.first();
        return !first.hasText() ? Optional.empty() : Optional.ofNullable(first.text());
    }

    private Optional<String> secondSelect(Elements currentSelect) {
        if (currentSelect.isEmpty()) return Optional.empty();
        return Optional.ofNullable(currentSelect.text());
    }
}