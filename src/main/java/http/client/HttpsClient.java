package http.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpsClient {

    public String getStringHtmlContentOfUrl(URL url) throws Exception {
        return IOUtils.toString(getHttpClient()
                .execute(new HttpGet(url.toURI()))
                .getEntity()
                .getContent(), StandardCharsets.UTF_8.name());
    }

    private CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(
                sslContext, new String[]{"TLSv1.2"}, null,
                new NoopHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslSf).build();
        HttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        return HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .build();
    }
}