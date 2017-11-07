package book.providers;

import http.client.HttpsClient;

import java.net.URL;

public class RemoteBookProvider implements BookProvider {

    private HttpsClient client;

    public RemoteBookProvider(HttpsClient client) {
        this.client = client;
    }

    @Override
    public String getBookHtml(URL url) throws Exception {
        return client.getStringHtmlContentOfUrl(url);
    }
}