package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YearOfPublishingFinderTest {

    private String searchYearOfPublishing = "2010";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div id=features>");
        testableHtml.append("<table>");
        testableHtml.append("<tr class=params>");
        testableHtml.append("<td class=attr>Рік видання</td>");
        testableHtml.append("<td class=value itemprop=identifier>");
        testableHtml.append(searchYearOfPublishing);
        testableHtml.append("</td>");
        testableHtml.append("</tr>");
        testableHtml.append("</table>");

        testableObject = new YearOfPublishingFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_year_of_publishing_method() throws PropertyNotFoundException {
        assertEquals(searchYearOfPublishing, testableObject.findProperty(doc).get());
    }
}