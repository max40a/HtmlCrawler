package aplication;

import convertors.BookConverter;
import book.providers.BookProvider;
import book.providers.LocalBookProvider;
import book.providers.RemoteBookProvider;
import http.client.HttpsClient;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import parser.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_LOCAL_LINKS = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\links.txt";
    private static final String LOG_PROPERTY_CONFIG_FILE = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\log\\log4j.properties";

    public static void main(String[] args) {
        PropertyConfigurator.configure(LOG_PROPERTY_CONFIG_FILE);

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

        BookProvider localProvider = new LocalBookProvider(converter);
        BookProvider remoteProvider = new RemoteBookProvider(new HttpsClient(), converter);

        try {
            FileUtils.readLines(new File(PATH_TO_LOCAL_LINKS), StandardCharsets.UTF_8.name())
                    .forEach(link -> {
                        try {
                            localProvider.getBook(new URL(link)).ifPresent(System.out::println);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        System.out.println();
        for (int i = 709055; i < 709075; i++) {
            String link = "https://nashformat.ua/products/-" + i;
            try {
                remoteProvider.getBook(new URL(link)).ifPresent(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }
    }
}