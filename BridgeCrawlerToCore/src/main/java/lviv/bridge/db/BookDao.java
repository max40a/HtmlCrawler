package lviv.bridge.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookDao {

    private static final String URL_TEMPLATE = "https://nashformat.ua/products/-%d";

    private QueryRunner runner;

    public BookDao(DataSource dataSource) {
        this.runner = new QueryRunner(dataSource);
    }

    public String getBookByUrl(int index) {
        String url = String.format(URL_TEMPLATE, index);
        String query = "SELECT book FROM crawler.books WHERE url=?";
        try {
            return runner.query(query, rs -> {
                rs.next();
                return rs.getString(1);
            }, url);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public void deleteBookByUrl(int index) {
        String url = String.format(URL_TEMPLATE, index);
        String query = "DELETE FROM crawler.books WHERE url=?";
        try {
            runner.update(query, url);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public List<Integer> getAllBooksUrlIndices() {
        String query = "SELECT url FROM crawler.books";
        ColumnListHandler<String> urlColumnListHandler = new ColumnListHandler<>("url");
        try {
            List<String> urls = runner.query(query, urlColumnListHandler);

            return urls.stream()
                    .map(str -> {
                        int start = str.lastIndexOf("-") + 1;
                        return Integer.parseInt(str.substring(start, str.length()));
                    })
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }
}
