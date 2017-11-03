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
    private String numberOfPages;
    private Isbn isbn;
    private List<String> categories = new ArrayList<>();
    private String description;
    private String price;
}
