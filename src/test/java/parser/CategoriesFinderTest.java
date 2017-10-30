package parser;

import org.hamcrest.collection.IsEmptyCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

public class CategoriesFinderTest {

    String firstSearchCategory = "FIRST TEST CATEGORY";
    String secondSearchCategory = "SECOND TEST CATEGORY";
    private PropertyFinder<List<String>> testableObject;
    private Document doc;

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<ul class=breadcrumb>");
        testableHtml.append("<li itemscope= itemtype=http://data-vocabulary.org/Breadcrumb>");
        testableHtml.append("<a itemprop=url href=/ data-language=252 title=Інтернет-магазин книг><span itemprop=\"title\">");
        testableHtml.append("Інтернет-магазин книг");
        testableHtml.append("</span></a>");
        testableHtml.append("<i class=fa fa-caret-right aria-hidden=true></i>");
        testableHtml.append("</li>");
        testableHtml.append("<li itemscope=\"\" itemtype=\"http://data-vocabulary.org/Breadcrumb\">");
        testableHtml.append(" <a itemprop=\"url\" href=\"catalog/biznes\" title=\"");
        testableHtml.append(firstSearchCategory);
        testableHtml.append("\"><span itemprop=\"title\">");
        testableHtml.append(firstSearchCategory);
        testableHtml.append("</span></a>\");");
        testableHtml.append("<li itemscope=\"\" itemtype=\"http://data-vocabulary.org/Breadcrumb\">");
        testableHtml.append(" <a itemprop=\"url\" href=\"catalog/biznes\" title=\"");
        testableHtml.append(secondSearchCategory);
        testableHtml.append("\"><span itemprop=\"title\">");
        testableHtml.append(secondSearchCategory);
        testableHtml.append("</span></a>\");");

        testableObject = new CategoriesFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_categories_method() {
        List<String> expectedList = Arrays.asList(firstSearchCategory, secondSearchCategory);
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(2));
    }

    @Test
    public void test_that_find_categories_method_not_return_empty_list_when_really_expected_list_with_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_categories_method_returned_reallyExpected_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, hasItems(firstSearchCategory, secondSearchCategory));
    }
}