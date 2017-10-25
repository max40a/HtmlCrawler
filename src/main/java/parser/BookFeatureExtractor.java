package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class BookFeatureExtractor {

    public static String findAttrInDocument(Document document, String wantedAttr) {
        Element features = document.getElementById("features");

        Elements keys = features.select("td.attr");
        Elements values = features.select("td.attr").nextAll();

        Map<String, String> attrs = new HashMap<>();
        Iterator<Element> keyIterator = keys.iterator();
        Iterator<Element> valueIterator = values.iterator();
        while (keyIterator.hasNext() && valueIterator.hasNext()) {
            attrs.put(keyIterator.next().text(), valueIterator.next().text());
        }
        return attrs.get(wantedAttr);
    }
}
