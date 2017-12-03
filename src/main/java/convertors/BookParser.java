package convertors;

import entity.Isbn;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookParser extends AbstractBookParser {

    private static final int MAX_NUMBER_OF_AUTHORS = 5;
    private final String STOP_SEARCH_AUTHORS_CRITERIA_REGEXP = "^[А-ЯЁЇІЄҐ]{2}.*";

    private final String SELECT_AUTHOR_CSS_QUERY_V1 = "div#annotation p > strong:matchesOwn(^ПРО АВТОР(А|А:|ІВ)), " +
            "div#annotation p > b:matchesOwn(^ПРО АВТОР(А|А:|ІВ))";
    private final String SELECT_AUTHOR_CSS_QUERY_V2 = "div#features tr.params > td.attr:matchesOwn(^Автор$)";
    private final String SELECT_DESCRIPTION_CSS_QUERY_V1 = "p > strong:matchesOwn(^ПРО КНИЖКУ$)";
    private final String SELECT_DESCRIPTION_CSS_QUERY_V2 = "div.tab-content > div#annotation > p";
    private final String CSS_QUERY_BY_GET_LANGUAGE = "div#features td.attr:contains(Мова)";
    private final String CSS_QUERY_BY_GET_NUMBER_OF_ISBN = "div#features td.attr:contains(ISBN)";
    private final String CSS_QUERY_BY_GET_NUMBER_OF_PAGES = "div#features td.attr:contains(Кількість сторінок)";
    private final String CSS_QUERY_BY_GET_PUBLISHING = "div#features td.attr:contains(Видавництво)";
    private final String CSS_QUERY_BY_GET_YEAR_OF_PULISHING = "div#features td.attr:contains(Рік видання)";

    @Override
    protected Optional<List<String>> findAuthors(Document document) {
        Elements select = document.select(SELECT_AUTHOR_CSS_QUERY_V2);
        if (!select.isEmpty()) {
            Elements searchElement = select.next();
            if (searchElement.isEmpty()) return Optional.empty();
            return Optional.of(Collections.singletonList(searchElement.text()));
        } else {
            select = document.select(SELECT_AUTHOR_CSS_QUERY_V1);
            if (select.isEmpty()) return Optional.empty();
            Elements searchElements = select.parents().next();
            if (searchElements.isEmpty()) return Optional.empty();
            return Optional.of(getAuthors(searchElements, 0));
        }
    }

    private List<String> getAuthors(Elements elements, int depth) {
        List<String> authors = new ArrayList<>();
        if (elements.text().matches(STOP_SEARCH_AUTHORS_CRITERIA_REGEXP) || depth > MAX_NUMBER_OF_AUTHORS) {
            return authors;
        }
        authors.add(elements.select("p > strong").text());
        authors.addAll(getAuthors(elements.next(), depth + 1));
        return authors;
    }

    @Override
    protected Optional<List<String>> findCategories(Document document) {
        return Optional.of(document
                .getElementsByClass("breadcrumb")
                .select("span[itemprop='title']")
                .stream()
                .skip(1)
                .map(Element::text)
                .collect(Collectors.toList()));
    }

    @Override
    protected Optional<String> findDescription(Document document) {
        Elements select = document.select(SELECT_DESCRIPTION_CSS_QUERY_V1);
        if (!select.isEmpty()) {
            Element first = select.parents().next().first();
            return !first.hasText() ? Optional.empty() : Optional.ofNullable(first.text());
        } else {
            return Optional.ofNullable(document.select(SELECT_DESCRIPTION_CSS_QUERY_V2).text());
        }
    }

    @Override
    protected Optional<Isbn> findIsbn(Document document) {
        return Optional.ofNullable(toIsbn(
                document.select(CSS_QUERY_BY_GET_NUMBER_OF_ISBN).next().text(),
                document.select(CSS_QUERY_BY_GET_LANGUAGE).next().text()));
    }

    private Isbn toIsbn(String number, String language) {
        String formattedIsbnNumber = formatIsbnNumber(number);
        return Isbn.builder()
                .language(language)
                .number(formattedIsbnNumber)
                .type(String.valueOf(formattedIsbnNumber.length()))
                .translation(true)
                .build();
    }

    private String formatIsbnNumber(String number) {
        return Stream.of(number.split("-"))
                .map(String::trim)
                .collect(Collectors.joining(""));
    }

    @Override
    protected Optional<String> findNumberOfPages(Document document) {
        return Optional.ofNullable(document.select(CSS_QUERY_BY_GET_NUMBER_OF_PAGES).next().text());
    }

    @Override
    protected Optional<String> findPrice(Document document) {
        Elements select = document.select("div > span.fn-price");
        if (select.isEmpty()) return Optional.empty();
        return Optional.ofNullable(select.first().text());
    }

    @Override
    protected Optional<String> findPublishing(Document document) {
        return Optional.ofNullable(document.select(CSS_QUERY_BY_GET_PUBLISHING).next().text());
    }

    @Override
    protected Optional<String> findTitle(Document document) {
        return Optional.of(getTitle(document.select("span[data-product]").first().text()));
    }

    private String getTitle(String s) {
        int start = s.indexOf("«") + 1;
        int finish = s.indexOf("»");
        return s.substring(start, finish).trim() + ".";
    }

    @Override
    protected Optional<String> findYearOfPublishing(Document document) {
        return Optional.ofNullable(document.select(CSS_QUERY_BY_GET_YEAR_OF_PULISHING).next().text());
    }
}