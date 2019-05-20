package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LinkPreprocessService {
    private final LinkParseService linkParseService;

    public void preprocessUri(String link) {
        linkParseService.parseLinks(2, Set.of(link));
    }
}
