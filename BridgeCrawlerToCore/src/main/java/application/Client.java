package application;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;

public class Client {

    private final String URL_TO_CORE = "http://localhost:8080/api/books";

    public void sendBookToCore(String book) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(URL_TO_CORE);

            StringEntity entity = new StringEntity(book, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            ResponseHandler<Optional<String>> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity httpEntity = response.getEntity();
                    return Optional.ofNullable(EntityUtils.toString(httpEntity));
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status + ", " + response.getStatusLine());
                }
            };

            client.execute(httpPost, responseHandler).ifPresent(System.out::println);
        }
    }
}