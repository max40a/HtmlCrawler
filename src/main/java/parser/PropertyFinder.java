package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public interface PropertyFinder {
    List<String> findProperty(Document document);
}
