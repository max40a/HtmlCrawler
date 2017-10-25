package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class PublishingFinder implements PropertyFinder<String> {

    private String attr = "Видавництво";

    @Override
    public Optional<String> findProperty(Document document) {
        return Optional.of(BookFeatureExtractor.findAttrInDocument(document, attr));
    }
}
