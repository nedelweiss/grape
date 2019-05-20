package com.github.kolegran.spdgoogle.index;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParsePageDto {
    private String title;
    private String body;
    private boolean index;
}
