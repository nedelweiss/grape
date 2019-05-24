package com.github.kolegran.spdgoogle.index;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ParsePageDto {
    @NonNull
    private String title;
    @NotNull
    private String body;
}
