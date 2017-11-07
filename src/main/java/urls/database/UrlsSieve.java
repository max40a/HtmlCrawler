package urls.database;

import http.client.HttpsClient;
import org.jsoup.Jsoup;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlsSieve {

    private static final String EVALUATE_TEMPLATE = "Сторінка не знайдена";

    private HttpsClient client;

    public UrlsSieve(HttpsClient client) {
        this.client = client;
    }

    public boolean doesBookExist(URL url) throws Exception {
        return !getTitleContent(this.getHtmlContent(url)).equals(EVALUATE_TEMPLATE);
    }

    private String getTitleContent(String html) {
        return Jsoup.parse(html, StandardCharsets.UTF_8.name()).title();
    }

    private String getHtmlContent(URL url) throws Exception {
        return this.client.getStringHtmlContentOfUrl(url);
    }
}