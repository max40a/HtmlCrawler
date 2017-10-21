package dropped.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File file1 = new File("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\1.html");
        File file2 = new File("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\2.html");
        File file3 = new File("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\3.html");
        File file4 = new File("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\4.html");
        File file5 = new File("C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\exempls\\5.html");

        Document document = Jsoup.parse(file3, "UTF-8");

        PropertyFinder authorsFinder = new AuthorsFinder();
        PropertyFinder priceFinder = new PriceFinder();
        PropertyFinder titleFinder = new TitleFinder();
        PropertyFinder descriptionFinder = new DescriptionFinder();

        System.out.println("Authors: ");
        authorsFinder.findProperty(document).forEach(System.out::println);
        System.out.println("Price : ");
        priceFinder.findProperty(document).forEach(System.out::println);
        System.out.println("Title: ");
        titleFinder.findProperty(document).forEach(System.out::println);
        System.out.println("Description: ");
        descriptionFinder.findProperty(document).forEach(System.out::println);
    }
}
