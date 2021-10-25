package com.github.kolegran.grape.index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DocumentDownloader {

    public Document downloadDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
