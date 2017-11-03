package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorsFinder implements PropertyFinder<List<String>> {

    private static final int MAX_NUMBER_OF_AUTHORS = 5;

    private String selectStr = "div#annotation p > strong:matchesOwn(^ПРО АВТОР(А|ІВ)), " +
            "div#annotation p > b:matchesOwn(^ПРО АВТОР(А|ІВ))";

    @Override
    public Optional<List<String>> findProperty(Document document) {
        Elements searchElements = document.select(selectStr).parents().next();
        List<String> authors = new ArrayList<>();
        authorSearch(searchElements, authors, 0);
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors);
    }

    private void authorSearch(Elements elements, List<String> list, int depth) {
        String stopSearchCriteriaRegExp = "^[А-ЯЁЇІЄҐ]{2}.*";
        if (elements.text().matches(stopSearchCriteriaRegExp) || depth > MAX_NUMBER_OF_AUTHORS) return;
        list.add(elements.select("p > strong").text());
        authorSearch(elements.next(), list, depth + 1);
    }
}
