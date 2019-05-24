package com.github.kolegran.spdgoogle.index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpConnectionService {
    public Document getConnection(String element) throws IOException {
        return Jsoup.connect(element).get();
    }
}
