package data.providers;

import entity.Book;

import java.net.URL;

public interface DataProvider {
    Book getListOfBooks(URL url) throws Exception;
}