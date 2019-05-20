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
public class PageParseService {
    private Map<String, ParsePageDto> pages = new HashMap<>();
    private Set<String> newLinks = new HashSet<>();

    public void parsePageByUrl(int deep, Set<String> links) {
        if (deep == 0) { return; }

        Set<Elements> elements = links.stream()
                .map(element -> {
                    Elements urls = null;

                    try {
                        if (!element.isEmpty()) {
                            Document document = Jsoup.connect(element).get();

                            ParsePageDto parsePageDto = ParsePageDto.builder()
                                    .title(document.title())
                                    .body(document.body().text())
                                    .index(false)
                                    .build();

                            pages.put(element, parsePageDto);

                            urls = document.body().select("a[href]");
                        }
                    } catch (IOException e) {
                        e.getMessage();
                    }
                    return urls;
                })
                .collect(Collectors.toSet());

        newLinks = elements.stream()
                .filter(Objects::nonNull)
                .flatMap(element -> element.stream()
                        .map(link -> link.attr("abs:href") + link.attr("rel")))
                .collect(Collectors.toSet());

        parsePageByUrl(deep - 1, newLinks);
    }

    public Map<String, ParsePageDto> getPages() {
        return pages;
    }
}
