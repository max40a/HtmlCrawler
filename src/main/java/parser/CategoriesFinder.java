package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFinder implements PropertyFinder{

    @Override
    public List<String> findProperty(Document document) {
        List<String> categories = new ArrayList<>();

        Elements elementsByClass = document.getElementsByClass("breadcrumb");
        Elements resultOfSelect = elementsByClass.select("span[itemprop='title']");
        for (int i = 1; i < resultOfSelect.size(); i++) {
            categories.add(resultOfSelect.get(i).text());
        }
        return categories;
    }
}
