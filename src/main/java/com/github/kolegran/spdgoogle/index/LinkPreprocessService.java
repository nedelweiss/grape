package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LinkPreprocessService {
    private final ParseService parseService;

    public void create(String link) {
        parseService.selectLinks(3, Set.of(link));
    }
}
