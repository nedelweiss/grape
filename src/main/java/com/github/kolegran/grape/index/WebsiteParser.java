package com.github.kolegran.grape.index;

import com.google.common.base.Stopwatch;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebsiteParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsiteParser.class);
    private static final String ATTR_KEY_ABS_REFERENCE = "abs:href";
    private static final String ATTR_KEY_REL_REFERENCE = "rel";
    private static final String CSS_QUERY = "a[href]";
    private static final String NUMBER_SIGN = "#";
    private static final int MIN_DEPTH = 1;

    private final DocumentDownloader documentDownloader;

    public WebsiteParser(DocumentDownloader documentDownloader) {
        this.documentDownloader = documentDownloader;
    }

    // TODO: Fix duplicates at the result
    public Map<String, ParsePageDto> parseByUrl(int depth, Set<String> links, Map<String, ParsePageDto> pages) {
        if (depth < MIN_DEPTH) {
            return pages;
        }

        final Stopwatch started = Stopwatch.createStarted();

        final Set<String> nestedLinks = links.parallelStream()
            .filter(link -> link.contains(Protocol.HTTP.getName()) || link.contains(Protocol.HTTPS.getName()))
            .map(link -> getElements(link, pages))
            .flatMap(element -> element.stream()
                .map(link -> link.attr(ATTR_KEY_ABS_REFERENCE) + link.attr(ATTR_KEY_REL_REFERENCE))
                .map(link -> link.contains(NUMBER_SIGN) ? link.substring(0, link.indexOf(NUMBER_SIGN)) : link)
                .filter(str -> !str.isEmpty()))
            .collect(Collectors.toSet());

        LOGGER.info("Spent time: {}. Depth is: {}", started.stop(), depth);

        return parseByUrl(depth - 1, nestedLinks, pages);
    }

    private Elements getElements(String link, Map<String, ParsePageDto> pages) {
        final Optional<Document> document = documentDownloader.downloadDocument(link);
        if (document.isPresent()) {
            final Document nonNullableDocument = document.get();
            final Element body = nonNullableDocument.body();
            if (body == null) {
                return new Elements();
            }
            pages.put(link, createParsedPage(nonNullableDocument));
            return body.select(CSS_QUERY);
        }
        return new Elements();
    }

    private ParsePageDto createParsedPage(Document document) {
        return new ParsePageDto(document.title(), document.body().text());
    }
}
