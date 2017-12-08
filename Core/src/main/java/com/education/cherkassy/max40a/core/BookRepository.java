package com.education.cherkassy.max40a.core;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
class BookRepository {

    private final String HOST_NAME = "localhost";
    private final int HOST_PORT = 9300;

    private final String INDEX_NAME = "core";
    private final String TYPE_NAME = "book";

    public int saveJsonBook(String jsonBook) throws UnknownHostException {
        IndexResponse indexResponse = client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(jsonBook, XContentType.JSON)
                .get();
        return indexResponse.status().getStatus();
    }

    public List<Map<String, Object>> getAllBooks() throws UnknownHostException {
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setFrom(0)
                .setSize(25)
                .get();

        return Arrays.stream(response.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }

    private Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_NAME), HOST_PORT));
    }
}