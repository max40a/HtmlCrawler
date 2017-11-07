package aplication;

import book.providers.BookProvider;
import book.providers.LocalBookProvider;
import book.providers.RemoteBookProvider;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import convertors.BookConverter;
import http.client.HttpsClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import parser.*;
import urls.database.UrlsGenerator;
import urls.database.UrlsSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_LOCAL_LINKS = "exempls/links.txt";
    private static final String LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";

    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";

    public static void main(String[] args) throws Exception {
        //URL resource = Main.class.getClassLoader().getResource(LOG_PROPERTY_CONFIG_FILE);
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource(LOG_PROPERTY_CONFIG_FILE));
        Properties dbProperties = loadProperties(PATH_TO_URL_PROVIDER_DB_PROPERTIES);

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

        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(dbProperties.getProperty("jdbc.url"));
        dataSource.setDatabaseName(dbProperties.getProperty("jdbc.dbname"));
        dataSource.setUser(dbProperties.getProperty("jdbc.username"));
        dataSource.setPassword(dbProperties.getProperty("jdbc.password"));

        List<String> links = IOUtils.readLines(Main.class.getClassLoader().getResourceAsStream(PATH_TO_LOCAL_LINKS), StandardCharsets.UTF_8.name());

        for (String link : links) {
            try {
                System.out.println(converter.convertHtmlToBook(localProvider.getBookHtml(new URL(link))));
            } catch (Exception e) {
                log.error(e);
            }
        }

        System.out.println();
        UrlsGenerator urlsGenerator = new UrlsGenerator(dataSource);
        int from = 709_000;
        int to = 709_100;

        urlsGenerator.generateUrls(from, to);

        List<URL> urls = new UrlsSupplier(dataSource)
                .getUrls()
                .parallelStream()
                .filter(Main.pageNotFoundFilter(client))
                .collect(Collectors.toList());

        for (URL url : urls) {
            try {
                System.out.println(converter.convertHtmlToBook(remoteProvider.getBookHtml(url)));
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private static Predicate<URL> pageNotFoundFilter(HttpsClient client) {
        return url -> {
            try {
                client.getStringHtmlContentOfUrl(url);
                return true;
            } catch (Exception e) {
                log.error(e);
                return false;
            }
        };
    }

    private static Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(in);
        }
        return properties;
    }
}