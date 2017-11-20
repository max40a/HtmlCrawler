package db.url;

import db.BadRequestException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import javax.sql.DataSource;
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

    public List<URL> getUrls() throws BadRequestException {
        String query = "SELECT url FROM crawler.urls WHERE `content present`!=false AND `retry` <= 5";
        try {
            return getListOfUrls(query);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public void changeRetryStatusSuccessCase(String urlToBook) throws BadRequestException {
        String query = "UPDATE crawler.urls SET `result`=true WHERE `url`=?";
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(query, urlToBook);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public void changeRetryStatusUnfortunateCase(String urlToBook) throws BadRequestException {
        String query = "UPDATE crawler.urls SET `result`=false, `retry`=(retry-?) WHERE `url`=?";
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update(query, 1, urlToBook);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    private List<URL> getListOfUrls(String query) throws SQLException {
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
            String message = String.format("Url creation error for this string : %s", s);
            throw new IllegalStateException(message, e);
        }
    }
}