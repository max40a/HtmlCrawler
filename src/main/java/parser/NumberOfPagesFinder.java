package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class NumberOfPagesFinder implements PropertyFinder<String> {

    private String attr = "Кількість сторінок";

    @Override
    public Optional<String> findProperty(Document document) {
        return Optional.of(BookFeatureExtractor.findAttrInDocument(document, attr));
    }
}
