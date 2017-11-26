package db.book;

import book.providers.BookProvider;
import com.google.gson.Gson;
import convertors.AbstractBookParser;
import db.url.UrlsSupplier;
import entity.Book;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Set;
import java.util.function.Consumer;

public class BookService {

    private static final Logger log = Logger.getLogger(BookService.class);
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";

    private UrlsSupplier urlsSupplier;
    private BookProvider bookProvider;
    private AbstractBookParser parser;
    private Gson bookToJsonConverter;
    private BookDao bookDao;
    private Validator validator;

    public BookService(UrlsSupplier urlsSupplier,
                       BookProvider bookProvider,
                       AbstractBookParser parser,
                       Gson bookToJsonConverter,
                       BookDao bookDao, Validator validator) {
        this.urlsSupplier = urlsSupplier;
        this.bookProvider = bookProvider;
        this.parser = parser;
        this.bookToJsonConverter = bookToJsonConverter;
        this.bookDao = bookDao;
        this.validator = validator;

        PropertyConfigurator.configure(getClass().getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));
    }

    public void executeService() {
        try {
            for (URL url : urlsSupplier.getUrls()) {
                bookProvider.getBookHtml(url).ifPresent(performBookTransformation(url));
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            log.error(stringWriter.toString());
        }
    }

    private Consumer<String> performBookTransformation(URL url) {
        return html -> {
            Book book = parser.convertHtmlToBook(html);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            if (violations.isEmpty()) {
                String bookJson = bookToJsonConverter.toJson(book);
                decideToSaveBook(url.toString(), bookJson);
                urlsSupplier.changeRetryStatusSuccessCase(url.toString());
            } else {
                urlsSupplier.changeRetryStatusUnfortunateCase(url.toString());
                for (ConstraintViolation<Book> violation : violations) {
                    log.info(violation + " : " + url.toString());
                }
            }
        };
    }

    private void decideToSaveBook(String urlToBook, String bookJson) {
        if (bookDao.isBookExist(urlToBook)) {
            bookDao.updateExistBook(urlToBook, bookJson);
        } else {
            bookDao.saveBook(urlToBook, bookJson);
        }
    }
}