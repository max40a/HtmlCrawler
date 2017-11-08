package urls.database;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UrlsGenerator {

    private final int NUMBER_OF_ATTEMPTS = 5;
    private final String URL_TEMPLATE = "https://nashformat.ua/products/-";

    private DataSource dataSource;
    private UrlsSieve sieve;

    public UrlsGenerator(DataSource dataSource, UrlsSieve sieve) {
        this.dataSource = dataSource;
        this.sieve = sieve;
    }

    public void generateUrls(int from, int to) throws SQLException, IOException {
        QueryRunner runner = new QueryRunner(dataSource);

        Stream.iterate(from, i -> i + 1)
                .limit(to - from + 1)
                .map(i -> URL_TEMPLATE + i)
                .peek(System.out::println)
                .filter(noExistBookUrlFilter())
                .forEach(saveToDb(runner));
    }

    private Consumer<String> saveToDb(QueryRunner runner) {
        String query = "INSERT INTO crawler.urls (url, retry) VALUES (?, ?)";

        return url -> {
            try {
                runner.update(query, url, NUMBER_OF_ATTEMPTS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }

    private Predicate<String> noExistBookUrlFilter() {
        return s -> {
            try {
                return sieve.doesBookExist(new URL(s));
            } catch (Exception e) {
                throw new IllegalStateException("Urls sieve broke down", e);
            }
        };
    }
}