package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final SearchIndexService searchIndexService;

    @GetMapping("/")
    public String getSearchBox() {
        return "searchBox";
    }

    @GetMapping("/search")
    public String search(@RequestParam
                         @NotBlank
                         @NotNull
                         @Size(min = 5, max = 200) String q, Map<String, Object> model) {

        List<PageDto> searchedPages = searchIndexService.searchIndex("body", q);
        model.put("searchedPages", searchedPages);

        return "searchedPages";
    }
}
