package aplication;

import convertors.BookConverter;
import data.providers.DataProvider;
import data.providers.LocalDataProvider;
import data.providers.RemoteDataProvider;
import http.client.HttpsClient;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import parser.*;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String LOCAL_LINKS = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\links.txt";
    private static final String PROPERTY_CONFIG_FILE = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\log\\log4j.properties";

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(PROPERTY_CONFIG_FILE);

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
        DataProvider localProvider = new LocalDataProvider(converter);
        RemoteDataProvider remoteProvider = new RemoteDataProvider(new HttpsClient(), converter);

        FileUtils.readLines(new File(LOCAL_LINKS), StandardCharsets.UTF_8.name())
                .forEach(link -> {
                    try {
                        System.out.println(localProvider.getListOfBooks(new URL(link)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info(e.getMessage());
                    }
                });

        System.out.println();
        for (int i = 709055; i < 709075; i++) {
            String link = "https://nashformat.ua/products/-" + i;
            System.out.println(remoteProvider.getListOfBooks(new URL(link)));
        }
    }

}