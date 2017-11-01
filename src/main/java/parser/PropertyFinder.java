package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

//TODO fixme
public interface PropertyFinder<T> {
    Optional<T> findProperty(Document document) throws PropertyNotFoundException;
}
