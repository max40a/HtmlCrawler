package entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class Book {
    @NotNull
    private String title;
    @NotNull
    @NotEmpty
    private List<String> authors = new ArrayList<>();
    @NotNull
    private String publishing;
    @NotNull
    private String yearOfPublishing;
    @NotNull
    private String numberOfPages;
    @Valid
    private Isbn isbn;
    @NotNull
    @NotEmpty
    private List<String> categories = new ArrayList<>();
    @NotNull
    private String description;
    @NotNull
    private String price;
}
