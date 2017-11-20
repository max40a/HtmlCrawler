package db.book;

import book.providers.BookProvider;
import com.google.gson.Gson;
import convertors.AbstractBookParser;
import db.url.UrlsSupplier;
import entity.Book;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import validate.BookValidator;

import java.net.URL;
import java.util.function.Consumer;

public class BookService {

    private static final Logger log = Logger.getLogger(BookService.class);
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";

    private UrlsSupplier urlsSupplier;
    private BookProvider bookProvider;
    private AbstractBookParser parser;
    private Gson bookToJsonConverter;
    private BookDao bookDao;

    public BookService(UrlsSupplier urlsSupplier,
                       BookProvider bookProvider,
                       AbstractBookParser parser,
                       Gson bookToJsonConverter,
                       BookDao bookDao) {
        this.urlsSupplier = urlsSupplier;
        this.bookProvider = bookProvider;
        this.parser = parser;
        this.bookToJsonConverter = bookToJsonConverter;
        this.bookDao = bookDao;

        PropertyConfigurator.configure(getClass().getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));
    }

    public void executeService() {
        try {
            for (URL url : urlsSupplier.getUrls()) {
                bookProvider.getBookHtml(url).ifPresent(performBookTransformation(url));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Consumer<String> performBookTransformation(URL url) {
        return html -> {
            Book book = parser.convertHtmlToBook(html);
            String bookJson = bookToJsonConverter.toJson(book);
            if (BookValidator.validateJson(bookJson, Book.class)) {
                bookDao.saveBook(url.toString(), bookJson);
                urlsSupplier.changeRetryStatusSuccessCase(url.toString());
            } else {
                urlsSupplier.changeRetryStatusUnfortunateCase(url.toString());
            }
        };
    }

}