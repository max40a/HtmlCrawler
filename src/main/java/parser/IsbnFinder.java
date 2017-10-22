package parser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class IsbnFinder implements PropertyFinder {

    private String[] foundProperties = {"ISBN", "Мова"};

    @Override
    public List<String> findProperty(Document document) {
        List<String> foundedProperties = new ArrayList<>();
        for (String prop : foundProperties) {
            foundedProperties.add(BookFeatureExtractor.findAttrInDocument(document, prop).get(0));
        }
        return foundedProperties;
    }
}
