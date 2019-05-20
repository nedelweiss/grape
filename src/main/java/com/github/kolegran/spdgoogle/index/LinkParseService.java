package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkParseService {
    private final IndexService indexService;

    public void selectLinks(int deep, Set<String> links) {
        if (deep == 0) { return; }

        Set<Elements> elements = links.stream()
                .map(element -> {
                    Elements uries = null;
                    try {
                        Document document = Jsoup.connect(element).get();
                        indexService.index(document.body().text());

                        uries = document.body().select("a[href]");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return uries;
                })
                .collect(Collectors.toSet());

        Set<String> newLinks = elements.stream()
                .flatMap(element -> element.stream()
                        .map(link -> link.attr("abs:href") + link.attr("rel")))
                .collect(Collectors.toSet());

        selectLinks(deep - 1, newLinks);
    }
}
