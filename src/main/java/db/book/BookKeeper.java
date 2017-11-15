package db.book;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BookKeeper {

    private DataSource dataSource;

    public BookKeeper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveBook(String urlToBook, String jsonBook) throws SQLException {
        String query = "INSERT INTO crawler.books (url, book) VALUES (?,?)";
        QueryRunner runner = new QueryRunner(dataSource);
        runner.update(query, urlToBook, jsonBook);
    }
}