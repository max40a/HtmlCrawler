package aplication;

import convertors.BookConverter;
import entity.Book;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parser.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\log\\log4j.properties");

        //TODO fixme
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

        String links = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\links.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(links))) {
            String path;
            while ((path = reader.readLine()) != null) {
                try {
                    Document document = Jsoup.parse(new File(path), "UTF-8");
                    Book book = converter.toBook(document);
                    System.out.println(book);
                } catch (PropertyNotFoundException e) {
                    System.out.println(e.getMessage());
                    log.info(e.getMessage());
                }
            }
        }
    }
}