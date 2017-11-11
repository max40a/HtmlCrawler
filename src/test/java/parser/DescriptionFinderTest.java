package parser;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DescriptionFinderTest {

    private static final String PATH_TO_TEST_DATA_V1 = "test_files/description_finder_test_file_v_1.html";
    private static final String PATH_TO_TEST_DATA_V2 = "test_files/description_finder_test_file_v_2.html";

    private PropertyFinder<String> testableObject;
    private Document docV1;
    private Document docV2;
    private String searchString = "SEARCHED TEST STRING";

    @Before
    public void setUp() throws IOException {
        testableObject = new DescriptionFinder();

        String testableHtmlV1 = getHtml(PATH_TO_TEST_DATA_V1);
        testableHtmlV1 = testableHtmlV1.replace("${test data}", searchString);
        docV1 = Jsoup.parse(testableHtmlV1, StandardCharsets.UTF_8.name());

        String testableHtmlV2 = getHtml(PATH_TO_TEST_DATA_V2);
        testableHtmlV2 = testableHtmlV2.replace("${test data}", searchString);
        docV2 = Jsoup.parse(testableHtmlV2, StandardCharsets.UTF_8.name());
    }

    private String getHtml(String pathToResource) throws IOException {
        URL resource = getClass().getClassLoader().getResource(pathToResource);
        return IOUtils.toString(resource, StandardCharsets.UTF_8.name());
    }

    @Test
    public void test_find_description_method_v1() {
        assertEquals(searchString, testableObject.findProperty(docV1).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_description_method_v2() {
        assertEquals(searchString, testableObject.findProperty(docV2).orElseThrow(IllegalStateException::new));
    }

    @After
    public void tearDown() throws Exception {
        testableObject = null;
        docV1 = null;
        docV2 = null;
    }
}