package com.education.cherkassy.max40a.core;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/books")
public class SimpleBookController {

    private final String HOST_NAME = "localhost";
    private final int HOST_PORT = 9300;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void getBook(@RequestBody String book) throws UnknownHostException {
        System.out.println(book);
        Client client = client();
        if (client != null) {
            System.out.println("Elastic Search Client got!");
        }
    }

    public Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_NAME), HOST_PORT));
    }
}