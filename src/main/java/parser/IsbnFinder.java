package parser;

import entity.Isbn;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

public class IsbnFinder implements PropertyFinder<Isbn> {

    private String cssQueryByGetLanguage = "div#features td.attr:contains(Мова)";
    private String cssQueryByGetNumberOfIsbn = "div#features td.attr:contains(ISBN)";

    @Override
    public Optional<Isbn> findProperty(Document document) {
        try {
            Elements selectOfLanguage = document.select(cssQueryByGetLanguage);
            Elements selectOfNumberIsbn = document.select(cssQueryByGetNumberOfIsbn);
            if (selectOfLanguage.isEmpty() || selectOfNumberIsbn.isEmpty()) return Optional.empty();

            Elements nextOfNumberIsbn = selectOfNumberIsbn.next();
            Elements nextOfLanguage = selectOfLanguage.next();
            if (nextOfLanguage.isEmpty() || nextOfNumberIsbn.isEmpty()) return Optional.empty();

            String language = nextOfLanguage.text();
            String isbnNumber = nextOfNumberIsbn.text();
            return Optional.of(toIsbn(isbnNumber, language));
        } catch (Exception e) {
            String message = String.format("%s could not find property for URL: %s, cause: %s",
                    this.getClass().getSimpleName(),
                    document.location(),
                    e.toString());
            throw new PropertyNotFoundException(message, e);
        }
    }

    private Isbn toIsbn(String number, String language) {
        String formattedIsbnNumber = isbnNumberFormatter(number);
        return Isbn.builder()
                .language(languageDetect(language))
                .number(formattedIsbnNumber)
                .type(String.valueOf(formattedIsbnNumber.length()))
                .translation(true)
                .build();
    }

    //TODO fixme
    private String languageDetect(String language) {
        switch (language) {
            case "Українська":
                return "UA";
            case "Російська":
                return "RU";
            case "Англійська":
                return "EN";
        }
        return "NOT DETECTED LANGUAGE";
    }

    private String isbnNumberFormatter(String inputIsbn) {
        return String.join("", inputIsbn.split("-"));
    }
}