package urls.database;


import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class UrlsGenerator {

    private final String DB_PROPERTIES = "C:\\Users\\Retro\\IdeaProjects\\HtmlCrawler\\src\\main\\resources\\db\\url.provider.properties";
    private final String query = "INSERT INTO crawler.urls (url, retry) VALUES (?, ?)";
    private final int FROM = 709_055;
    private final int TO = 709_075;
    private final int NUMBER_OF_ATTEMPTS = 5;
    private final String URL_TEMPLATE = "https://nashformat.ua/products/-";

    public void generateUrls() throws SQLException, IOException {
        getConnection().ifPresent(connection -> {
            QueryRunner runner = new QueryRunner();
            List<String> urls = prepareUrl(URL_TEMPLATE, FROM, TO);
            for (String url : urls) {
                try {
                    runner.update(connection, query, url, NUMBER_OF_ATTEMPTS);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<String> prepareUrl(String template, int from, int to) {
        ArrayList<String> preparedUrls = new ArrayList<>();
        for (int i = from; i < to; i++) {
            preparedUrls.add(template + i);
        }
        return preparedUrls;
    }

    private Optional<Connection> getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(DB_PROPERTIES))) {
            properties.load(in);
        }
        DbUtils.loadDriver(properties.getProperty("jdbc.drivers"));
        return Optional.ofNullable(DriverManager.getConnection(
                properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.username"),
                properties.getProperty("jdbc.password")
        ));
    }
}