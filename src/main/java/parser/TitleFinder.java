package parser;

import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;

public class TitleFinder implements PropertyFinder{

    @Override
    public List<String> findProperty(Document document) {
        String select = document.select("span[data-product]").first().text();
        String title = getTitle(select);
        return Collections.singletonList(title);
    }

    private static String getTitle(String s) {
        int start = s.indexOf("«") + 1;
        int finish = s.indexOf("»");
        return s.substring(start, finish).trim() + ".";
    }
}