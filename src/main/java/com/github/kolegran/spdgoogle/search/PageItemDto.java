package com.github.kolegran.spdgoogle.search;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageItemDto {
    private String url;
    private String title;
    private String body;
}
