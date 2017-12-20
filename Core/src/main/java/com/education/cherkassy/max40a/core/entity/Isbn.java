package com.education.cherkassy.max40a.core.entity;

import lombok.Data;

@Data
public class Isbn {
    private String language;
    private String number;
    private String type;
    private Boolean translation;
}