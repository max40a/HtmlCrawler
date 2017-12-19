package com.education.cherkassy.max40a.core;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Map<String, Object>> getAllBooks(int page) throws UnknownHostException {
        int numberOfBooks = 3;
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setFrom(page * numberOfBooks)
                .setSize(numberOfBooks)
                .get();

        return Arrays.stream(response.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getBooksByCategories(String category) throws UnknownHostException {
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.matchQuery("categories", category))
                .setFrom(0)
                .setSize(5)
                .get();

        return getListMapsOfResults(response);
    }

    public List<Map<String, Object>> getBooksByAuthor(String authorName) throws UnknownHostException {
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.matchQuery("authors", authorName))
                .setFrom(0)
                .setSize(5)
                .get();

        return getListMapsOfResults(response);
    }

    public Set<String> getAllCategories() throws UnknownHostException {
        return getBookFieldValues("categories");
    }

    public Set<String> getAllAuthors() throws UnknownHostException {
        return getBookFieldValues("authors");
    }

    private Set<String> getBookFieldValues(String fieldName) throws UnknownHostException {
        SearchResponse response = client().prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .addAggregation(
                        AggregationBuilders.terms(fieldName).size(10_000).field(fieldName)
                )
                .setFrom(0)
                .setSize(10_000)
                .execute()
                .actionGet();

        Terms terms = response.getAggregations().get(fieldName);
        return terms.getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsString)
                .flatMap(str -> Stream.of(str.split(",")))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private List<Map<String, Object>> getListMapsOfResults(SearchResponse response) {
        return Arrays.stream(response.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }

    private Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_NAME), HOST_PORT));
    }
}