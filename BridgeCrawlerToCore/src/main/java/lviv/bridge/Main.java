package lviv.bridge;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import lviv.bridge.db.BookDao;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure(Main.class.getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));

            Properties dbProperties = loadProperties(PATH_TO_URL_PROVIDER_DB_PROPERTIES);
            MysqlConnectionPoolDataSource dataSource = initDataSource(dbProperties);

            BookDao bookDao = new BookDao(dataSource);
            Client client = new Client();

            for (Integer index : bookDao.getAllBooksUrlIndices()) {
                String jsonBook = bookDao.getBookByUrl(index);
                Integer responseCode = client.sendBookToCore(jsonBook);
                if (responseCode >= 200 && responseCode < 300) {
                    bookDao.deleteBookByUrl(index);
                } else {
                    String message = String.format("The book could not be sent to the application Core. Cause: %d server response code", responseCode);
                    throw new SendEntityException(message);
                }
            }
        } catch (SendEntityException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
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
