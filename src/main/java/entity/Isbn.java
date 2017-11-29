package entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Isbn {
    @NotNull
    private String language;
    @NotNull
    @Length(min = 10, max = 13)
    private String number;
    @NotNull
    private String type;
    @NotNull
    private Boolean translation;
}
