package parser.alterntive;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import parser.PropertyFinder;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DescriptionFinder2Test {

    private PropertyFinder<String> testableObject;
    private Document doc;
    private String searchString = "TEST DESCRIPTION";

    @Before
    public void setUp() throws IOException {
        String pathToTestData = "test_files/description_finder_2_test_file.html";
        URL resource = getClass().getClassLoader().getResource(pathToTestData);
        String testableHtml = IOUtils.toString(resource, StandardCharsets.UTF_8);
        testableHtml = testableHtml.replace("${test data}", searchString);
        testableObject = new DescriptionFinder2();
        doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
    }

    @Test
    public void test_find_description_method() {
        assertEquals(searchString, testableObject.findProperty(doc).get());
    }
}