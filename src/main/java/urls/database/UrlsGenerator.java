package urls.database;


import org.apache.commons.dbutils.QueryRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UrlsGenerator {

    private final int NUMBER_OF_ATTEMPTS = 5;
    private final String URL_TEMPLATE = "https://nashformat.ua/products/-";

    private ConnectionUtil connectionUtil;

    public UrlsGenerator(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    public void generateUrls(int from, int to) throws SQLException, IOException {
        String query = "INSERT INTO crawler.urls (url, retry) VALUES (?, ?)";
        connectionUtil.getConnection().ifPresent(connection -> {
            QueryRunner runner = new QueryRunner();
            List<String> urls = prepareUrl(URL_TEMPLATE, from, to);
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
}