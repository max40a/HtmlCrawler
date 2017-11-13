package book.providers;

import db.url.UrlSieve;
import http.client.HttpsClient;

import java.net.URL;
import java.util.Optional;

public class RemoteBookProvider implements BookProvider {

    private HttpsClient client;
    private UrlSieve sieve;

    public RemoteBookProvider(HttpsClient client, UrlSieve sieve) {
        this.client = client;
        this.sieve = sieve;
    }

    @Override
    public Optional<String> getBookHtml(URL url) throws Exception {
        String html = client.getStringHtmlContentOfUrl(url);
        return sieve.sieveOfHtml(html, url);
    }
}