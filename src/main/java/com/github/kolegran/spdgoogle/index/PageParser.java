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
    private final HttpConnectionService httpConnectionService;

    public Map<String, ParsePageDto> parsePageByUrl(int depth, Set<String> links) {
        if (depth == 0) { return pages; }

        Set<Elements> elements = links.stream()
                .map(element -> {
                    Elements urls = null;
                    try {
                            Document document = httpConnectionService.getConnection(element);
                            ParsePageDto parsePageDto = ParsePageDto.builder()
                                    .title(document.title())
                                    .body(document.body().text())
                                    .build();

                            pages.put(element, parsePageDto);

                            urls = document.body().select("a[href]");
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                    return urls;
                })
                .collect(Collectors.toSet());

        Set<String> newLinks = elements.stream()
                .filter(Objects::nonNull)
                .flatMap(element -> element.stream()
                        .map(link -> link.attr("abs:href") + link.attr("rel"))
                        .map(link -> link.contains("#") ? link.substring(0, link.indexOf("#")) : link)
                        .filter(str -> !str.isEmpty()))
                .collect(Collectors.toSet());

        return parsePageByUrl(depth - 1, newLinks);
    }
}
