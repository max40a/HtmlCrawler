package urls.database;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlsSupplier {

    private ConnectionUtil connectionUtil;

    public UrlsSupplier(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    //TODO fixme
    public List<Optional<?>> getUrls() throws IOException, SQLException {
        String query = "SELECT url from crawler.urls";
        if (connectionUtil.getConnection().isPresent()) {
            List<String> gotUrls = new QueryRunner().query(
                    connectionUtil.getConnection().get(),
                    query,
                    new ColumnListHandler<String>("url"));
            return gotUrls.stream()
                    .map(s -> {
                        try {
                            return Optional.of(new URL(s));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return Optional.empty();
                        }
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}