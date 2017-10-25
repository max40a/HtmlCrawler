package entity;

import lombok.Data;

@Data
public class Isbn {
    private Integer isbnId;
    private Integer bookId;
    private String language;
    private String number;
    private String type;
    private Boolean translation;
}
