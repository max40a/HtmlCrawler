package lviv.bridge;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Client {

    private final String URL_TO_CORE = "http://localhost:8080/api/books";

    public Integer sendBookToCore(String book) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(URL_TO_CORE);

            StringEntity entity = new StringEntity(book, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            ResponseHandler<Integer> responseHandler = response -> response.getStatusLine().getStatusCode();
            return client.execute(httpPost, responseHandler);
        }
    }
}