package application;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";

    public static void main(String[] args) throws IOException {
        Properties dbProperties = loadProperties(PATH_TO_URL_PROVIDER_DB_PROPERTIES);
        MysqlConnectionPoolDataSource dataSource = initDataSource(dbProperties);

        BookDao d = new BookDao(dataSource);
        Client client = new Client();

        String url = "https://nashformat.ua/products/-709015";
        try {
            String bookByUrl = d.getBookByUrl(url);
            client.foo(bookByUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MysqlConnectionPoolDataSource initDataSource(Properties dbProperties) throws IOException {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(dbProperties.getProperty("jdbc.url"));
        dataSource.setDatabaseName(dbProperties.getProperty("jdbc.dbname"));
        dataSource.setUser(dbProperties.getProperty("jdbc.username"));
        dataSource.setPassword(dbProperties.getProperty("jdbc.password"));
        dataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return dataSource;
    }

    private static Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(in);
        }
        return properties;
    }
}
