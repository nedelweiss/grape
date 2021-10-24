package com.github.kolegran.grape.index;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageParser {
    private static final String CSS_QUERY = "a[href]";
    private static final String ATTR_KEY_ABS_REFERENCE = "abs:href";
    private static final String ATTR_KEY_REL_REFERENCE = "rel";
    private static final String NUMBER_SIGN = "#";

    private Map<String, ParsePageDto> pages = new HashMap<>();
    private final HttpService httpService;

    public Map<String, ParsePageDto> parsePageByUrl(int depth, Set<String> links) {
        if (depth < 1) { return pages; }

        Set<String> nestedLinks = links.stream()
                .map(link -> {
                    Elements urls;
                    try {
                        Document document = httpService.downloadDocument(link);
                        pages.put(link, createParsePage(document));
                        urls = document.body().select(CSS_QUERY);
                    } catch (IOException | NullPointerException e) {
                        return new Elements();
                    }
                    return urls;
                })
                .flatMap(element -> element.stream()
                        .map(link -> link.attr(ATTR_KEY_ABS_REFERENCE) + link.attr(ATTR_KEY_REL_REFERENCE))
                        .map(link -> link.contains(NUMBER_SIGN) ? link.substring(0, link.indexOf(NUMBER_SIGN)) : link)
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
