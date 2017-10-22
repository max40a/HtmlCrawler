package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public class PublishingFinder implements PropertyFinder {

    private String attr = "Видавництво";

    @Override
    public List<String> findProperty(Document document) {
        return BookFeatureExtractor.findAttrInDocument(document, attr);
    }
}
