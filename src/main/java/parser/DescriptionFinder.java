package parser;

import org.jsoup.nodes.Document;

import java.util.*;

public class DescriptionFinder extends AbstractPropertyFinder implements PropertyFinder<String> {

    private String searchCriteria = "ПРО КНИЖКУ";

    @Override
    public Optional<String> findProperty(Document document) {
        List<String> parsedStrings = getParsedStrings(document, "p");
        int start = findStart(parsedStrings, searchCriteria);
        int finish = findFinish(parsedStrings, start);
        //TODO fixme
        List<String> properties = getProperties(parsedStrings, start, finish);
        String description = properties.get(0);
        if(description.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(description);
    }
}