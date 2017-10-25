package parser;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Optional;

public class AuthorsFinder extends AbstractPropertyFinder implements PropertyFinder<List<String>> {

    private String searchCriteria = "ПРО АВТОР";

    @Override
    public Optional<List<String>> findProperty(Document document) {
        List<String> parsedStrings = getParsedStrings(document, "p > strong");
        int start = findStart(parsedStrings, searchCriteria);
        int finish = findFinish(parsedStrings, start);
        //TODO fixme
        List<String> authors = getProperties(parsedStrings, start, finish);
        if (authors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(authors);
    }
}