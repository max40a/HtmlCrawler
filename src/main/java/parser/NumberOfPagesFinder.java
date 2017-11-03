package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class NumberOfPagesFinder implements PropertyFinder<String> {

    private String cssQuery = "div#features td.attr:contains(Кількість сторінок)";

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            String numbersOfPage = document.select(cssQuery).next().text();
            return (numbersOfPage.isEmpty()) ? Optional.empty() : Optional.of(numbersOfPage);
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }
}