package aplication;

import book.providers.BookProvider;
import book.providers.LocalBookProvider;
import book.providers.RemoteBookProvider;
import com.google.gson.Gson;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import convertors.BookConverter;
import db.book.BookKeeper;
import db.url.UrlsGenerator;
import db.url.UrlsSieve;
import db.url.UrlsSupplier;
import entity.Book;
import http.client.HttpsClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_LOCAL_LINKS = "exempls/links.txt";
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";
    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));

        Map<String, PropertyFinder> searchEngines = new LinkedHashMap<>();
        searchEngines.put("title", new TitleFinder());
        searchEngines.put("authors", new AuthorsFinder());
        searchEngines.put("publishing", new PublishingFinder());
        searchEngines.put("yearOfPublishing", new YearOfPublishingFinder());
        searchEngines.put("numberOfPages", new NumberOfPagesFinder());
        searchEngines.put("Isbn", new IsbnFinder());
        searchEngines.put("categories", new CategoriesFinder());
        searchEngines.put("description", new DescriptionFinder());
        searchEngines.put("price", new PriceFinder());

        BookConverter converter = new BookConverter(searchEngines);
        HttpsClient client = new HttpsClient();

        BookProvider localProvider = new LocalBookProvider();
        BookProvider remoteProvider = new RemoteBookProvider(client);

        List<String> links = IOUtils.readLines(
                Main.class.getClassLoader().getResourceAsStream(PATH_TO_LOCAL_LINKS),
                StandardCharsets.UTF_8.name());

        for (String link : links) {
            try {
                System.out.println(converter.convertHtmlToBook(localProvider.getBookHtml(new URL(link))));
            } catch (Exception e) {
                log.error(e);
            }
        }

        Properties dbProperties = loadProperties(PATH_TO_URL_PROVIDER_DB_PROPERTIES);
        MysqlConnectionPoolDataSource dataSource = initDataSource(dbProperties);

        System.out.println();
        UrlsSieve urlsSieve = new UrlsSieve(new HttpsClient());
        UrlsGenerator urlsGenerator = new UrlsGenerator(dataSource, urlsSieve);
        int from = 706_000;
        int to = 706_500;
        urlsGenerator.generateUrls(from, to);

        List<URL> urls = new UrlsSupplier(dataSource).getUrls();
        Gson bookToJsonConverter = new Gson();
        BookKeeper bookKeeper = new BookKeeper(dataSource);

        for (URL url : urls) {
            try {
                Book book = converter.convertHtmlToBook(remoteProvider.getBookHtml(url));
                System.out.println(book);
                bookKeeper.saveBook(bookToJsonConverter.toJson(book), url);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private static MysqlConnectionPoolDataSource initDataSource(Properties dbProperties) throws IOException {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(dbProperties.getProperty("jdbc.url"));
        dataSource.setDatabaseName(dbProperties.getProperty("jdbc.dbname"));
        dataSource.setUser(dbProperties.getProperty("jdbc.username"));
        dataSource.setPassword(dbProperties.getProperty("jdbc.password"));
        dataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return dataSource;
    }

    private static Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(in);
        }
        return properties;
    }
}