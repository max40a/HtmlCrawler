package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorsFinder implements PropertyFinder<List<String>> {

    private String selectStr = "div#annotation p > strong:matchesOwn(^ПРО АВТОР(А|ІВ))";

    @Override
    public Optional<List<String>> findProperty(Document document) throws PropertyNotFoundException {
        try {
            Elements searchElements = document.select(selectStr).parents().next();
            List<String> authors = new ArrayList<>();
            authorSearch(searchElements, authors, 0);
            return authors.isEmpty() ? Optional.empty() : Optional.of(authors);
        } catch (Exception e) {
            String message = String.format("\"AUTHORS\" Not Found for URL: %s, cause: %s", document.location(), e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }

    private void authorSearch(Elements elements, List<String> list, int stopSearch) {
        String stopSearchCriteriaRegExp = "^[А-ЯЁЇІЄҐ]{2}.*";
        if (elements.text().matches(stopSearchCriteriaRegExp) || stopSearch++ >= 5) return;
        list.add(elements.select("p > strong").text());
        authorSearch(elements.next(), list, stopSearch);
    }
}
