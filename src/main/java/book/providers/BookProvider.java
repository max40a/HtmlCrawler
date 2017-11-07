package book.providers;

import java.net.URL;

public interface BookProvider {
    String getBookHtml(URL url) throws Exception;
}