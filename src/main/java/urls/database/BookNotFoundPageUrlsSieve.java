package urls.database;

import http.client.HttpsClient;

import java.net.URL;

public class BookNotFoundPageUrlsSieve {

    private HttpsClient client;

    public BookNotFoundPageUrlsSieve(HttpsClient client) {
        this.client = client;
    }

    public boolean doesBookExist(URL url) {
        return true;
    }
}