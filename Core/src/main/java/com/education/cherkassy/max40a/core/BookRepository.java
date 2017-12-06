package com.education.cherkassy.max40a.core;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Repository
class BookRepository {

    private final String HOST_NAME = "localhost";
    private final int HOST_PORT = 9300;

    private final String INDEX_NAME = "crawler";
    private final String TYPE_NAME = "book";

    public int saveJsonBook(String jsonBook) throws UnknownHostException {
        IndexResponse indexResponse = client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(jsonBook, XContentType.JSON)
                .get();
        return indexResponse.status().getStatus();
    }

    public List<String> getAllBooks() throws UnknownHostException {
        List<String> books = new ArrayList<>();
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .get();

        SearchHits hits = response.getHits();
        for (SearchHit documentFields : hits.getHits()) {
            books.add(documentFields.getSourceAsString());
        }

        return books;
    }

    private Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_NAME), HOST_PORT));
    }
}