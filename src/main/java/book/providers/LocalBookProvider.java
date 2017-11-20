package book.providers;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LocalBookProvider implements BookProvider {

    @Override
    public Optional<String> getBookHtml(URL url) throws Exception {
        return Optional.ofNullable(IOUtils.toString(url.toURI(), StandardCharsets.UTF_8.name()));
    }
}