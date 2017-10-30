package entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Book {
    private Integer id;
    private String title;
    private List<String> authors = new ArrayList<>();
    private String publishing;
    private String yearOfPublishing;
    private Short numberOfPages;
    private Isbn isbn;
    private List<String> categories = new ArrayList<>();
    private String description;
    private Double price;
}
