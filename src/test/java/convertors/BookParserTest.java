package convertors;

import entity.Isbn;
import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.Is;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import static org.junit.Assert.assertEquals;

public class BookParserTest extends BookParser {

    private static final String TEST_DATA = "Lorem ipsum dolor sit amet";

    private String getHtml(String pathToResource) throws IOException {
        URL resource = getClass().getClassLoader().getResource(pathToResource);
        assert resource != null;
        return IOUtils.toString(resource, StandardCharsets.UTF_8.name());
    }

    @Test
    public void test_find_authors_method_v1() throws IOException {
        String testableHtmlV1 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_1.html");
        testableHtmlV1 = testableHtmlV1.replace("${first test author}", TEST_DATA);
        testableHtmlV1 = testableHtmlV1.replace("${second test author}", TEST_DATA);
        testableHtmlV1 = testableHtmlV1.replace("${third test author}", TEST_DATA);
        Document docV1 = Jsoup.parse(testableHtmlV1, StandardCharsets.UTF_8.name());

        List<String> expectedList = Arrays.asList(TEST_DATA, TEST_DATA, TEST_DATA);
        List<String> actualList = this.findAuthors(docV1).orElseThrow(IllegalStateException::new);
        assertThat(actualList, is(expectedList));
        assertThat(actualList, hasSize(3));
    }

    @Test
    public void test_find_authors_method_v2() throws IOException {
        String testableHtmlV2 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_2.html");
        testableHtmlV2 = testableHtmlV2.replace("${test author}", TEST_DATA);
        Document docV2 = Jsoup.parse(testableHtmlV2, StandardCharsets.UTF_8.name());

        List<String> expectedList = Collections.singletonList(TEST_DATA);
        List<String> actualList = this.findAuthors(docV2).orElseThrow(IllegalStateException::new);
        assertThat(expectedList, is(actualList));
        assertThat(actualList, hasSize(1));
    }

    @Test
    public void test_that_find_authors_V1_method_not_return_empty_list_when_really_expected_list_with_content() throws IOException {
        String testableHtmlV1 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_1.html");
        testableHtmlV1 = testableHtmlV1.replace("${first test author}", TEST_DATA);
        testableHtmlV1 = testableHtmlV1.replace("${second test author}", TEST_DATA);
        Document docV1 = Jsoup.parse(testableHtmlV1, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findAuthors(docV1).orElseThrow(IllegalStateException::new);
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_that_find_authors_V2_method_not_return_empty_list_when_really_expected_list_with_content() throws IOException {
        String testableHtmlV2 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_2.html");
        testableHtmlV2 = testableHtmlV2.replace("${test author}", TEST_DATA);
        Document docV2 = Jsoup.parse(testableHtmlV2, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findAuthors(docV2).orElseThrow(IllegalStateException::new);
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_authors_method_V1_returned_reallyExpected_content() throws IOException {
        String testableHtmlV1 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_1.html");
        testableHtmlV1 = testableHtmlV1.replace("${first test author}", TEST_DATA);
        testableHtmlV1 = testableHtmlV1.replace("${second test author}", TEST_DATA);
        Document docV1 = Jsoup.parse(testableHtmlV1, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findAuthors(docV1).orElseThrow(IllegalStateException::new);
        assertThat(actualList, hasItems(TEST_DATA, TEST_DATA));
    }

    @Test
    public void test_find_authors_method_V2_returned_reallyExpected_content() throws IOException {
        String testableHtmlV2 = getHtml("test_files/book_parser_test_files/author_finder_test_file_v_2.html");
        testableHtmlV2 = testableHtmlV2.replace("${test author}", TEST_DATA);
        Document docV2 = Jsoup.parse(testableHtmlV2, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findAuthors(docV2).orElseThrow(IllegalStateException::new);
        assertThat(actualList, hasItems(TEST_DATA));
    }

    @Test
    public void test_find_categories_method() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/category_find_test_file.html");
        testableHtml = testableHtml.replace("${first test category}", TEST_DATA);
        testableHtml = testableHtml.replace("${second test category}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());

        List<String> expectedList = Arrays.asList(TEST_DATA, TEST_DATA);
        List<String> actualList = this.findCategories(doc).orElseThrow(IllegalStateException::new);
        assertThat(expectedList, Is.is(actualList));
        assertThat(actualList, hasSize(2));
    }

    @Test
    public void test_that_find_categories_method_not_return_empty_list_when_really_expected_list_with_content() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/category_find_test_file.html");
        testableHtml = testableHtml.replace("${first test category}", TEST_DATA);
        testableHtml = testableHtml.replace("${second test category}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findCategories(doc).orElseThrow(IllegalStateException::new);
        assertThat(actualList, not(IsEmptyCollection.empty()));
    }

    @Test
    public void test_find_categories_method_returned_reallyExpected_content() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/category_find_test_file.html");
        testableHtml = testableHtml.replace("${first test category}", TEST_DATA);
        testableHtml = testableHtml.replace("${second test category}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());

        List<String> actualList = this.findCategories(doc).orElseThrow(IllegalStateException::new);
        assertThat(actualList, hasItems(TEST_DATA, TEST_DATA));
    }

    @Test
    public void test_find_description_method_v1() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/description_find_test_file_v1.html");
        testableHtml = testableHtml.replace("${test description data}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findDescription(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_description_method_v2() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/description_find_test_file_v2.html");
        testableHtml = testableHtml.replace("${test description data}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findDescription(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_how_does_isbn_finder_find_isbn() throws IOException {
        String searchLanguage = "Українська";
        String searchIsbnNumber = "978-617-7388-83-7";

        String testableHtml = getHtml("test_files/book_parser_test_files/isbn_find_test_file.html");
        testableHtml = testableHtml.replace("${test language}", searchLanguage);
        testableHtml = testableHtml.replace("${test number}", searchIsbnNumber);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());

        Isbn expectedIsbn = Isbn.builder().language("Українська").number("9786177388837").type("13").translation(true).build();
        assertEquals(expectedIsbn, findIsbn(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_number_of_pages_method() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/number_of_pages_test_file.html");
        testableHtml = testableHtml.replace("${test number of pages}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findNumberOfPages(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_year_price_method() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/price_find_test_file.html");
        testableHtml = testableHtml.replace("${test price}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findPrice(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_publishing_method() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/publishing_find_test_file.html");
        testableHtml = testableHtml.replace("${test publishing}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findPublishing(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_title_method() {
        String testableHtml = String.format("<span data-product=20068 itemprop=name style=display: none;>«%s»<span>", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA + ".", findTitle(doc).orElseThrow(IllegalStateException::new));
    }

    @Test
    public void test_find_year_of_publishing_method() throws IOException {
        String testableHtml = getHtml("test_files/book_parser_test_files/year_of_publishing_test_file.html");
        testableHtml = testableHtml.replace("${test year of publishing}", TEST_DATA);
        Document doc = Jsoup.parse(testableHtml, StandardCharsets.UTF_8.name());
        assertEquals(TEST_DATA, findYearOfPublishing(doc).orElseThrow(IllegalStateException::new));
    }
}