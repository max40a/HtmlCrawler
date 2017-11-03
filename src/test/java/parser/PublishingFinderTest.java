package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PublishingFinderTest {

    private String searchPublishingString = "TEST STRING";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div id=features>");
        testableHtml.append("<table>");
        testableHtml.append("<tr class=params>");
        testableHtml.append("<td class=attr>Видавництво</td>");
        testableHtml.append("<td class=value itemprop=identifier>");
        testableHtml.append(searchPublishingString);
        testableHtml.append("</td>");
        testableHtml.append("</tr>");
        testableHtml.append("</table>");

        testableObject = new PublishingFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_publishing_method() {
        assertEquals(searchPublishingString, testableObject.findProperty(doc).get());
    }
}