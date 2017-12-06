package com.education.cherkassy.max40a.core;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Repository
class BookRepository {

    private final String HOST_NAME = "localhost";
    private final int HOST_PORT = 9300;

    private final String INDEX_NAME = "core";
    private final String TYPE_NAME = "book";

    public int saveJsonBook(String jsonBook) throws UnknownHostException {
        Client client = client();
        Objects.requireNonNull(client);
        IndexResponse indexResponse = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(jsonBook, XContentType.JSON)
                .get();
        return indexResponse.status().getStatus();
    }

    private Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_NAME), HOST_PORT));
    }
}