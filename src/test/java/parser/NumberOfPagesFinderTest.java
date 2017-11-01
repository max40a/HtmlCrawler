package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberOfPagesFinderTest {

    private String searchNumberOfPages = "100";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div id=features>");
        testableHtml.append("<table>");
        testableHtml.append("<tr class=params>");
        testableHtml.append("<td class=attr>Кількість сторінок</td>");
        testableHtml.append("<td class=value itemprop=identifier>");
        testableHtml.append(searchNumberOfPages);
        testableHtml.append("</td>");
        testableHtml.append("</tr>");
        testableHtml.append("</table>");

        testableObject = new NumberOfPagesFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_number_of_pages_method() throws PropertyNotFoundException {
        assertEquals(searchNumberOfPages, testableObject.findProperty(doc).get());
    }
}