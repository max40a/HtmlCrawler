package entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookValidationTest {

    private Book testBook;
    private Validator validator;

    @Before
    public void setUp() {
        testBook = new Book();
        testBook.setTitle("TITLE");
        testBook.setAuthors(Arrays.asList("AUTHOR_1", "AUTHOR_2"));
        testBook.setPublishing("PUBLISHING");
        testBook.setYearOfPublishing("2017");
        testBook.setNumberOfPages("100");
        testBook.setIsbn(Isbn.builder().translation(true).type("13").number("9999999999999").language("LANGUAGE").build());
        testBook.setCategories(Arrays.asList("CATEGORY_1", "CATEGORY_2"));
        testBook.setDescription("DESCRIPTION");
        testBook.setPrice("Price");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test_valid_completely_book_entity() {
        Set<ConstraintViolation<Book>> validate = validator.validate(testBook);
        assertTrue(validate.size() == 0);
    }

    @Test
    public void test_valid_book_entity_when_it_has_empty_authors_list() {
        testBook.setAuthors(Collections.emptyList());
        Set<ConstraintViolation<Book>> validate = validator.validate(testBook);
        assertTrue(validate.size() > 0);
        assertEquals("не может быть пусто", validate.iterator().next().getMessage());
    }

    @Test
    public void test_valid_book_entity_when_it_has_empty_string_in_authors_list() {
        testBook.setAuthors(Collections.singletonList(""));
        Set<ConstraintViolation<Book>> validate = validator.validate(testBook);
        assertTrue(validate.size() > 0);
        assertEquals("List cannot contain empty fields", validate.iterator().next().getMessage());
    }

    @After
    public void tearDown() {
        testBook = null;
        validator = null;
    }
}