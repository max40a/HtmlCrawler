package urls.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ConnectionUtil {

    private String pathToProperty;

    public ConnectionUtil(String pathToProperty) {
        this.pathToProperty = pathToProperty;
    }

    public Optional<Connection> getConnection() throws IOException, SQLException {
        return Optional.ofNullable(DriverManager.getConnection(
                loadProperties().getProperty("jdbc.url"),
                loadProperties().getProperty("jdbc.username"),
                loadProperties().getProperty("jdbc.password")
        ));
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(pathToProperty))) {
            properties.load(in);
        }
        return properties;
    }
}