package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PriceFinderTest {
    private String searchPrice = "150";
    private PropertyFinder<String> testableObject;
    private Document doc;

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div class=h4 font-weight-bold price>");
        testableHtml.append("<span content=Ціна: data-language=328>Ціна:</span>");
        testableHtml.append("<span class=fn-price itemprop=price content=130>");
        testableHtml.append(searchPrice);
        testableHtml.append("</span>");
        testableHtml.append(" <span itemprop=priceCurrency content=UAH>грн</span>");
        testableHtml.append("</div>");

        testableObject = new PriceFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_year_price_method() {
        assertEquals(searchPrice, testableObject.findProperty(doc).get());
    }
}