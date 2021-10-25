package com.github.kolegran.grape.index;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebsiteParser {

    private static final String CSS_QUERY = "a[href]";
    private static final String ATTR_KEY_ABS_REFERENCE = "abs:href";
    private static final String ATTR_KEY_REL_REFERENCE = "rel";
    private static final String NUMBER_SIGN = "#";

    private final Map<String, ParsePageDto> pages = new HashMap<>();
    private final DocumentDownloader documentDownloader;

    public WebsiteParser(DocumentDownloader documentDownloader) {
        this.documentDownloader = documentDownloader;
    }

    public Map<String, ParsePageDto> parsePageByUrl(int depth, Set<String> links) {
        if (depth < 1) {
            return pages;
        }

        Set<String> nestedLinks = links.stream()
            .map(link -> {
                Elements urls;
                try {
                    Document document = documentDownloader.downloadDocument(link);
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
        return new ParsePageDto(document.title(), document.body().text());
    }
}
