package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoriesFinder implements PropertyFinder<List<String>> {

    @Override
    public Optional<List<String>> findProperty(Document document) {
        return Optional.of(document
                .getElementsByClass("breadcrumb")
                .select("span[itemprop='title']")
                .stream()
                .skip(1)
                .map(Element::text)
                .collect(Collectors.toList()));
    }
}
