package urls.database;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UrlsSupplier {

    private DataSource dataSource;

    public UrlsSupplier(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<URL> getUrls() throws IOException, SQLException {
        String query = "SELECT url from crawler.urls";

        ColumnListHandler<String> urlColumnMapper = new ColumnListHandler<>("url");

        return new QueryRunner().query(dataSource.getConnection(), query, urlColumnMapper)
                .stream()
                .map(this::stringToUrl)
                .collect(Collectors.toList());
    }

    private URL stringToUrl(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Url creation error", e);
        }
    }
}