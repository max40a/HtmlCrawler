package convertors;

import entity.Book;
import entity.Isbn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBookParser {

    protected abstract Optional<List<String>> findAuthors(Document document);

    protected abstract Optional<List<String>> findCategories(Document document);

    protected abstract Optional<String> findDescription(Document document);

    protected abstract Optional<Isbn> findIsbn(Document document);

    protected abstract Optional<String> findNumberOfPages(Document document);

    protected abstract Optional<String> findPrice(Document document);

    protected abstract Optional<String> findPublishing(Document document);

    protected abstract Optional<String> findTitle(Document document);

    protected abstract Optional<String> findYearOfPublishing(Document document);

    public Book convertHtmlToBook(String html) {
        Document document = Jsoup.parse(html, StandardCharsets.UTF_8.name());
        Book book = new Book();

        findAuthors(document).ifPresent(book::setAuthors);
        findCategories(document).ifPresent(book::setCategories);
        findDescription(document).ifPresent(book::setDescription);
        findIsbn(document).ifPresent(book::setIsbn);
        findNumberOfPages(document).ifPresent(book::setNumberOfPages);
        findPrice(document).ifPresent(book::setPrice);
        findPublishing(document).ifPresent(book::setPublishing);
        findTitle(document).ifPresent(book::setTitle);
        findYearOfPublishing(document).ifPresent(book::setYearOfPublishing);

        return book;
    }
}