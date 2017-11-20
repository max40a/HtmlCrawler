package db.url;

import org.apache.commons.dbutils.QueryRunner;
import org.jsoup.Jsoup;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;

public class UrlSieve {

    private static final String EVALUATE_TEMPLATE = "Сторінка не знайдена";

    private DataSource dataSource;

    public UrlSieve(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<String> sieveOfHtml(String html, String pathToHtml) throws SQLException {
        String title = Jsoup.parse(html, StandardCharsets.UTF_8.name()).title();
        if (title.equals(EVALUATE_TEMPLATE)) {
            String query = "UPDATE crawler.urls SET `content present`=false WHERE `url`=?";
            QueryRunner runner = new QueryRunner(dataSource);
            runner.update(query, pathToHtml);
            return Optional.empty();
        }
        return Optional.of(html);
    }
}