package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Isbn {
    private Integer isbnId;
    private Integer bookId;
    private String language;
    private String number;
    private String type;
    private Boolean translation;
}
