package com.github.kolegran.grape.search;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

import static com.github.kolegran.grape.IndexSearchConstants.BODY;

@Controller
@Validated
public class SearchController {

    private final SearchIndexService searchIndexService;

    public SearchController(SearchIndexService searchIndexService) {
        this.searchIndexService = searchIndexService;
    }

    @GetMapping("/")
    public String getStartSearchPage() {
        return "search";
    }

    @GetMapping("/search")
    public String search(
        @RequestParam @NotBlank @NotNull @Size(min = 1, max = 200) String q,
        @RequestParam(defaultValue = "relevant") String sortType,
        @RequestParam(defaultValue = "1") int pageNum, Map<String, Object> model
    ) {
        model.put("page", searchIndexService.searchIndex(BODY, q, sortType, pageNum));
        model.put("currentPage", pageNum);
        return "searchedPages";
    }
}
