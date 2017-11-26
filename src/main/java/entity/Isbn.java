package entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Isbn {
    @NotNull
    private String language;
    @NotNull
    private String number;
    @NotNull
    private String type;
    @NotNull
    private Boolean translation;
}
