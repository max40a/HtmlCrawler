package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class YearOfPublishingFinder implements PropertyFinder<String> {

    private String attr = "Рік видання";

    @Override
    public Optional<String> findProperty(Document document) {
        return Optional.of(BookFeatureExtractor.findAttrInDocument(document, attr));
    }
}
