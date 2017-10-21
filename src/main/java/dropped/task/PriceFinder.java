package dropped.task;

import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;

public class PriceFinder implements PropertyFinder {

    @Override
    public List<String> findProperty(Document document) {
        String price = document.select("div > span.fn-price").first().text();
        return Collections.singletonList(price);
    }
}
