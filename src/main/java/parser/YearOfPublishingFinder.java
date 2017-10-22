package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public class YearOfPublishingFinder implements PropertyFinder {

    private String attr = "Рік видання";

    @Override
    public List<String> findProperty(Document document) {
        return BookFeatureExtractor.findAttrInDocument(document, attr);
    }
}
