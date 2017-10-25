package parser;

import org.jsoup.nodes.Document;

import java.util.Optional;

public class TitleFinder implements PropertyFinder<String>{

    @Override
    public Optional<String> findProperty(Document document) {
        String select = document.select("span[data-product]").first().text();
        if(select.isEmpty()) {
            return Optional.empty();
        }
        String title = getTitle(select);
        return Optional.of(title);
    }

    private static String getTitle(String s) {
        int start = s.indexOf("«") + 1;
        int finish = s.indexOf("»");
        return s.substring(start, finish).trim() + ".";
    }
}