package com.github.kolegran.grape.index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class DocumentDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDownloader.class);

    public Optional<Document> downloadDocument(String url) {
        try {
            LOGGER.info("Success url: {}", url);
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException exception) {
            LOGGER.error("{}. Handled url: {}", exception.getMessage(), url);
            return Optional.empty();
        }
    }
}
