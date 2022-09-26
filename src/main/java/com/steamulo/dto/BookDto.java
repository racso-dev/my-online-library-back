package com.steamulo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {
    private String title;
    private String sumup;
    private String publicationYear;
    private String category;
    private String cover;
    private String id;
}
