package entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Book {
    private Integer id;
    private String title;
    private List<String> authors = new ArrayList<>();
    private String publishing;
    private String yearOfPublishing;
    private Short numberOfPages;
    private List<Isbn> isbnList = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private String description;
    private Double price;
}
