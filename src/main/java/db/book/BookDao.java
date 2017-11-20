package db.book;

import db.BadRequestException;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BookDao {

    private DataSource dataSource;

    public BookDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveBook(String urlToBook, String jsonBook) throws BadRequestException {
        String query = "INSERT INTO crawler.books (url, book) VALUES (?,?)";
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(query, urlToBook, jsonBook);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }
}