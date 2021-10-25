package com.github.kolegran.grape.index;

import java.util.Objects;

public class ParsePageDto {

    private final String title;
    private final String body;

    public ParsePageDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsePageDto that = (ParsePageDto) o;
        return Objects.equals(title, that.title) &&
            Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body);
    }

    @Override
    public String toString() {
        return "ParsePageDto{" +
            "title='" + title + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
