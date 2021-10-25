package com.github.kolegran.grape.index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DocumentDownloader {

    public Document downloadDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException exception) {
            throw new CannotConnectToURLException("Cannot connect to the next URL: " + url, exception);
        }
    }

    private static final class CannotConnectToURLException extends RuntimeException {

        public CannotConnectToURLException(String message, Exception exception) {
            super(message, exception);
        }
    }
}
