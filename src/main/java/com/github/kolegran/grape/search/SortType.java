package com.github.kolegran.grape.search;

public enum SortType {

    RELEVANT("relevant"),
    ALPHABET("alphabet"),
    ;

    private final String type;

    SortType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
