package parser;

import entity.Isbn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IsbnFinderTest {

    private PropertyFinder<Isbn> testableObject;
    private Document doc;
    private String searchLanguage = "Українська";
    private String searchIsbnNumber = "978-617-7388-83-7";

    @Before
    public void setUp() throws Exception {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div id=features>");
        testableHtml.append("<table>");
        testableHtml.append("<tr class=params>");
        testableHtml.append("<td class=attr>Мова</td>");
        testableHtml.append("<td class=value itemprop=identifier>");
        testableHtml.append(searchLanguage);
        testableHtml.append("</td>");
        testableHtml.append("</tr>");
        testableHtml.append("<tr class=params>");
        testableHtml.append("<td class=attr>ISBN</td>");
        testableHtml.append("<td class=value itemprop=identifier>");
        testableHtml.append(searchIsbnNumber);
        testableHtml.append("</td>");
        testableHtml.append("</tr>");
        testableHtml.append("</table>");
        testableHtml.append("</div>");

        testableObject = new IsbnFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_how_does_isbn_finder_find_isbn() {
        Isbn expectedIsbn = Isbn.builder().language("UA").number("9786177388837").type("13").translation(true).build();
        assertEquals(expectedIsbn, testableObject.findProperty(doc).get());
    }
}