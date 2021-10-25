package com.github.kolegran.grape.search;

import java.util.Objects;

public class PageItemDto {

    private final String url;
    private final String title;
    private final String body;
    private final String fragments;

    public PageItemDto(String url, String title, String body, String fragments) {
        this.url = url;
        this.title = title;
        this.body = body;
        this.fragments = fragments;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getFragments() {
        return fragments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageItemDto that = (PageItemDto) o;
        return Objects.equals(url, that.url) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body) &&
            Objects.equals(fragments, that.fragments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, body, fragments);
    }

    @Override
    public String toString() {
        return "PageItemDto{" +
            "url='" + url + '\'' +
            ", title='" + title + '\'' +
            ", body='" + body + '\'' +
            ", fragments='" + fragments + '\'' +
            '}';
    }
}
