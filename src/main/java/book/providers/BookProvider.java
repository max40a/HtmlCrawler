package book.providers;

import java.net.URL;
import java.util.Optional;

public interface BookProvider {
    Optional<String> getBookHtml(URL url) throws Exception;
}