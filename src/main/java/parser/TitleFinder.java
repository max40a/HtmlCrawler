package parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class TitleFinder implements PropertyFinder<String> {

    @Override
    public Optional<String> findProperty(Document document) {
        try {
            Elements select = document.select("span[data-product]");
            return (select.isEmpty()) ? Optional.empty() : Optional.of(getTitle(select.first().text()));
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }

    private String getTitle(String s) {
        int start = s.indexOf("«") + 1;
        int finish = s.indexOf("»");
        return s.substring(start, finish).trim() + ".";
    }
}