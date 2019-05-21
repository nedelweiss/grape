package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchIndexService searchIndexService;

    @GetMapping("/")
    public String getSearchBox() {
        return "searchBox";
    }

    @PostMapping("/search")
    public String search(@RequestParam
                         @NotBlank
                         @Size(min = 5, max = 200) String q, Map<String, Object> model) {

        List<PageDto> searchedPages = searchIndexService.searchIndex("body", q);
        model.put("searchedPages", searchedPages);

        return "searchedPages";
    }
}
