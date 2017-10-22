package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public class AuthorsFinder extends AbstractPropertyFinder implements PropertyFinder {

    private String searchCriteria = "ПРО АВТОР";

    @Override
    public List<String> findProperty(Document document) {
        List<String> parsedStrings = getParsedStrings(document, "p > strong");
        int start = findStart(parsedStrings, searchCriteria);
        int finish = findFinish(parsedStrings, start);
        return getProperties(parsedStrings, start, finish);
    }
}