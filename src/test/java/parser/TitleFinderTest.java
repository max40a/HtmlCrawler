package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TitleFinderTest {

    private String currentTitle = "Book «TEST TITLE»";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<span data-product=20068 itemprop=name style=display: none;>");
        testableHtml.append(currentTitle);
        testableHtml.append("<span>");


        testableObject = new TitleFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_title_method() {
        String expectedTitle = "TEST TITLE.";
        assertEquals(expectedTitle, testableObject.findProperty(doc).get());
    }
}