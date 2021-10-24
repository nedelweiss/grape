package com.github.kolegran.grape.search;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageItemDto {
    private String url;
    private String title;
    private String body;
    private String fragments;
}
