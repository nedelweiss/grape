package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageParser {
    private Map<String, ParsePageDto> pages = new HashMap<>();
    private final HttpService httpService;

    public Map<String, ParsePageDto> parsePageByUrl(int depth, Set<String> links) {
        if (depth == 0) { return pages; }

        Set<String> nestedLinks = links.stream()
                .map(link -> {
                    Elements urls = new Elements();
                    try {
                        Document document = httpService.downloadDocument(link);
                        pages.put(link, createParsePage(document));
                        urls = document.body().select("a[href]");
                    } catch (IOException e) {
                        new Elements();
                    }
                    return urls;
                })
                .filter(Objects::nonNull)
                .flatMap(element -> element.stream()
                        .map(link -> link.attr("abs:href") + link.attr("rel"))
                        .map(link -> link.contains("#") ? link.substring(0, link.indexOf("#")) : link)
                        .filter(str -> !str.isEmpty()))
                .collect(Collectors.toSet());

        return parsePageByUrl(depth - 1, nestedLinks);
    }

    private ParsePageDto createParsePage(Document document) {
        return ParsePageDto.builder()
                .title(document.title())
                .body(document.body().text())
                .build();
    }
}
