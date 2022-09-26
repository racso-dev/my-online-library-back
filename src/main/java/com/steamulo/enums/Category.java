package com.steamulo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    LITERATURE("literature", "Littérature"),
    COMICS("comics", "Bande dessinée"),
    UTILITY("utility", "Utilitaire"),
    CHILDREN("children", "Livre pour enfant");

    private String endpoint;
    private String categoryName;
}
