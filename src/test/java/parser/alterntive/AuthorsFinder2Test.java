package parser.alterntive;

import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import parser.PropertyFinder;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class AuthorsFinder2Test {

    private PropertyFinder<List<String>> testableObject;
    private Document doc;
    private String testAuthor = "TEST AUTHOR";

    @Before
    public void setUp() throws IOException {
        String pathToTestData = "test_files/author_finder_2_test_file.html";
        URL resource = getClass().getClassLoader().getResource(pathToTestData);
        String testableHtml = IOUtils.toString(resource, StandardCharsets.UTF_8);
        testableHtml = testableHtml.replace("${test data}", testAuthor);

        testableObject = new AuthorsFinder2();
        doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
    }

    @Test
    public void test_find_authors_method() {
        List<String> expectedList = Collections.singletonList(testAuthor);
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(1));
    }

    @Test
    public void test_that_find_authors_method_not_return_empty_list_when_really_expected_list_with_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_authors_method_returned_reallyExpected_content() {
        List<String> actualList = testableObject.findProperty(doc).get();
        assertThat(actualList, hasItems("TEST AUTHOR"));
    }
}