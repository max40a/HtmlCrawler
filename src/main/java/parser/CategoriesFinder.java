package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriesFinder implements PropertyFinder<List<String>>{

    @Override
    public Optional<List<String>> findProperty(Document document) {
        List<String> categories = new ArrayList<>();

        Elements elementsByClass = document.getElementsByClass("breadcrumb");
        Elements resultOfSelect = elementsByClass.select("span[itemprop='title']");
        for (int i = 1; i < resultOfSelect.size(); i++) {
            categories.add(resultOfSelect.get(i).text());
        }
        if(categories.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(categories);
    }
}
