package parser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IsbnFinder implements PropertyFinder<List<String>> {

    private String[] foundProperties = {"ISBN", "Мова"};

    @Override
    public Optional<List<String>> findProperty(Document document) {
        List<String> foundedProperties = new ArrayList<>();
        for (String prop : foundProperties) {
            foundedProperties.add(BookFeatureExtractor.findAttrInDocument(document, prop));
        }
        if (foundedProperties.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(foundedProperties);
    }
}
