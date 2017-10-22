package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public class NumberOfPagesFinder implements PropertyFinder {

    private String attr = "Кількість сторінок";

    @Override
    public List<String> findProperty(Document document) {
        return BookFeatureExtractor.findAttrInDocument(document, attr);
    }
}
