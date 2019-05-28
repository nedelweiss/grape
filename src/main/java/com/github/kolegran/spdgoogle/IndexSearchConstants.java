package com.github.kolegran.spdgoogle;

import org.springframework.stereotype.Component;

@Component
public final class IndexSearchConstants {
    private IndexSearchConstants() {
    }

    public static String BODY = "body";
    public static String TITLE = "title";
    public static String URL = "url";
    public static String SORT_BY_FIELD = "sortByTitle";
}
