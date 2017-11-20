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

public class BookParser extends AbstractBookParser {

    private static final int MAX_NUMBER_OF_AUTHORS = 5;

    private final String SELECT_AUTHOR_CSS_QUERY_V1 = "div#annotation p > strong:matchesOwn(^ПРО АВТОР(А|ІВ)), " +
            "div#annotation p > b:matchesOwn(^ПРО АВТОР(А|ІВ))";
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
        Elements select = document.select(SELECT_AUTHOR_CSS_QUERY_V1);
        if (!select.isEmpty()) {
            Elements searchElements = select.parents().next();
            if (searchElements.isEmpty()) return Optional.empty();
            List<String> authors = new ArrayList<>();
            authorSearch(searchElements, authors, 0);
            return authors.isEmpty() ? Optional.empty() : Optional.of(authors);
        } else {
            select = document.select(SELECT_AUTHOR_CSS_QUERY_V2);
            if (select.isEmpty()) return Optional.empty();
            Elements searchElement = select.next();
            if (searchElement.isEmpty()) return Optional.empty();
            return Optional.of(Collections.singletonList(searchElement.text()));
        }
    }

    private void authorSearch(Elements elements, List<String> authors, int depth) {
        String stopSearchCriteriaRegExp = "^[А-ЯЁЇІЄҐ]{2}.*";
        if (elements.text().matches(stopSearchCriteriaRegExp) || depth > MAX_NUMBER_OF_AUTHORS) return;
        authors.add(elements.select("p > strong").text());
        authorSearch(elements.next(), authors, depth + 1);
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
        return String.join("", number.split("-"));
    }

    @Override
    protected Optional<String> findNumberOfPages(Document document) {
        return Optional.ofNullable(document.select(CSS_QUERY_BY_GET_NUMBER_OF_PAGES).next().text());
    }

    @Override
    protected Optional<String> findPrice(Document document) {
        return Optional.ofNullable(document.select("div > span.fn-price").first().text());
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