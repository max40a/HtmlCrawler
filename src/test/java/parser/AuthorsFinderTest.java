package parser;

import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class AuthorsFinderTest {

    private static final String PATH_TO_TEST_DATA_V1 = "test_files/author_finder_test_file_v_1.html";
    private static final String PATH_TO_TEST_DATA_V2 = "test_files/author_finder_test_file_v_2.html";

    private PropertyFinder<List<String>> testableObject;
    private Document docV1;
    private Document docV2;
    private String firstTestAuthor = "FIRST TEST AUTHOR";
    private String secondTestAuthor = "SECOND TEST AUTHOR";

    @Before
    public void setUp() throws IOException {
        testableObject = new AuthorsFinder();

        String testableHtmlV1 = getHtml(PATH_TO_TEST_DATA_V1);
        testableHtmlV1 = testableHtmlV1.replace("${first test author}", firstTestAuthor);
        testableHtmlV1 = testableHtmlV1.replace("${second test author}", secondTestAuthor);
        docV1 = Jsoup.parse(testableHtmlV1, StandardCharsets.UTF_8.name());

        String testableHtmlV2 = getHtml(PATH_TO_TEST_DATA_V2);
        testableHtmlV2 = testableHtmlV2.replace("${test author}", firstTestAuthor);
        docV2 = Jsoup.parse(testableHtmlV2, StandardCharsets.UTF_8.name());
    }

    private String getHtml(String pathToResource) throws IOException {
        URL resource = getClass().getClassLoader().getResource(pathToResource);
        return IOUtils.toString(resource, StandardCharsets.UTF_8.name());
    }

    @Test
    public void test_find_authors_method_V1() {
        List<String> expectedList = Arrays.asList(firstTestAuthor, secondTestAuthor);
        List<String> actualList = testableObject.findProperty(docV1).orElseThrow(IllegalStateException::new);
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(2));
    }

    @Test
    public void test_find_authors_method_V2() {
        List<String> expectedList = Collections.singletonList(firstTestAuthor);
        List<String> actualList = testableObject.findProperty(docV2).orElseThrow(IllegalStateException::new);
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(1));
    }

    @Test
    public void test_that_find_authors_V1_method_not_return_empty_list_when_really_expected_list_with_content() {
        List<String> actualList = testableObject.findProperty(docV1).orElseThrow(IllegalStateException::new);
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_that_find_authors_V2_method_not_return_empty_list_when_really_expected_list_with_content() {
        List<String> actualList = testableObject.findProperty(docV2).orElseThrow(IllegalStateException::new);
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_authors_method_V1_returned_reallyExpected_content() {
        List<String> actualList = testableObject.findProperty(docV1).orElseThrow(IllegalStateException::new);
        assertThat(actualList, hasItems(firstTestAuthor, secondTestAuthor));
    }

    @Test
    public void test_find_authors_method_V2_returned_reallyExpected_content() {
        List<String> actualList = testableObject.findProperty(docV2).orElseThrow(IllegalStateException::new);
        assertThat(actualList, hasItems(firstTestAuthor));
    }

    @After
    public void tearDown() throws Exception {
        testableObject = null;
        docV1 = null;
        docV2 = null;
    }
}