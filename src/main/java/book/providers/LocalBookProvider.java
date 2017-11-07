package book.providers;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LocalBookProvider implements BookProvider {

    @Override
    public String getBookHtml(URL url) throws Exception {
        return IOUtils.toString(url.toURI(), StandardCharsets.UTF_8.name());
    }
}