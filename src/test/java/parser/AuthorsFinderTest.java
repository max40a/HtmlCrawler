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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class AuthorsFinderTest {

    private PropertyFinder<List<String>> testableObject;
    private Document doc;
    private String firstTestAuthor = "FIRST TEST AUTHOR";
    private String secondTestAuthor = "SECOND TEST AUTHOR";

    @Before
    public void setUp() {
        StringBuilder testableHtml = new StringBuilder();
        testableHtml.append("<div class=tab-pane p-x-1 collapse in active id=annotation role=tabpanel itemprop=description>");
        testableHtml.append("<p>Орієнтовна дата надходження &mdash; 06.11.2017р.</p>");
        testableHtml.append("<p><strong>ТЕМАТИКА</strong></p>");
        testableHtml.append("<p>Суспільствознавство, соціальна поведінка, історія, економіка, політика, право.</p>");
        testableHtml.append("<p><strong>ПРО АВТОРІВ</strong></p>");
        testableHtml.append("<p><strong>");
        testableHtml.append(firstTestAuthor);
        testableHtml.append("</strong>");
        testableHtml.append("some text</p>");
        testableHtml.append("<p><strong>");
        testableHtml.append(secondTestAuthor);
        testableHtml.append("</strong>");
        testableHtml.append("some text</p>");
        testableHtml.append("<p><strong>ВІДГУКИ ПРО КНИЖКУ</strong></p>>");

        testableObject = new AuthorsFinder();
        doc = Jsoup.parse(testableHtml.toString(), "UTF-8");
    }

    @Test
    public void test_find_authors_method() {
        List<String> expectedList = Arrays.asList(firstTestAuthor, secondTestAuthor);
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(2));
    }

    @Test
    public void test_that_find_authors_method_not_return_empty_list_when_really_expected_list_with_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_authors_method_returned_reallyExpected_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, hasItems(firstTestAuthor, secondTestAuthor));
    }
}
