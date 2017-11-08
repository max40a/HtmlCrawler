package db.book;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.SQLException;

public class BookKeeper {

    private DataSource dataSource;

    public BookKeeper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveBook(String jsonBook, URL bookUrl) throws SQLException {
        String query = "INSERT INTO crawler.books (url, book) VALUES (?,?)";
        QueryRunner runner = new QueryRunner(dataSource);
        runner.update(query, bookUrl.toString(), jsonBook);
    }
}