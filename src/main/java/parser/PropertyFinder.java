package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface PropertyFinder<T> {
    Optional<T> findProperty(Document document);
}
