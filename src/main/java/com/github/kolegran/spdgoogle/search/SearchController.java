package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchController {
    @GetMapping("/")
    public String getSearchBox() {
        return "searchBox";
    }
}
