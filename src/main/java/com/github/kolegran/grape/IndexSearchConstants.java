package com.github.kolegran.grape;

import org.springframework.stereotype.Component;

@Component
public final class IndexSearchConstants {

    private IndexSearchConstants() {
        // hide public constructor
    }

    public final static String BODY = "body";
    public final static String TITLE = "title";
    public final static String URL = "url";
    public final static String SORT_BY_TITLE = "sortByTitle";
}
