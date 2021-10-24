package com.github.kolegran.grape.index;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParsePageDto {
    private String title;
    private String body;
}
