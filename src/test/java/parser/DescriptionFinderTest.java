package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DescriptionFinderTest {

    private String searchString = "Searched Test String";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div class=tab-pane p-x-1 collapse in active id=annotation role=tabpanel itemprop=description>");
        testableHtml.append("<p><strong>ПРО КНИЖКУ</strong></p>");
        testableHtml.append("<p>");
        testableHtml.append(searchString);
        testableHtml.append("</p>");
        testableHtml.append("<p><strong>ТЕМАТИКА</strong></p>");
        testableHtml.append("<p>Суспільствознавство, соціальна поведінка, історія, економіка, політика, право.</p>");

        testableObject = new DescriptionFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_findProperty_method() throws PropertyNotFoundException {
        assertEquals(searchString, testableObject.findProperty(doc).get());
    }
}