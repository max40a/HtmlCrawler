package application;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class BookDao {

    private QueryRunner runner;

    public BookDao(DataSource dataSource) {
        this.runner = new QueryRunner(dataSource);
    }

    public List<String> getAllBooks() throws SQLException {
        String query = "SELECT book FROM crawler.books";
        ColumnListHandler<String> booksColumnMapper = new ColumnListHandler<>("book");
        return runner.query(query, booksColumnMapper);
    }

    public String getBookByUrl(String url) throws SQLException {
        String query = "SELECT book FROM crawler.books WHERE url=?";
        return runner.query(query, rs -> {
            rs.next();
            return rs.getString(1);
        }, url);
    }
}
