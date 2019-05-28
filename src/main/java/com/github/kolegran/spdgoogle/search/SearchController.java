package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

import static com.github.kolegran.spdgoogle.IndexSearchConstants.BODY;

@Controller
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final SearchIndexService searchIndexService;

    @GetMapping("/")
    public String getSearchBox() {
        return "search";
    }

    @GetMapping("/search")
    public String search(@RequestParam
                         @NotBlank
                         @NotNull
                         @Size(min = 1, max = 200) String q,
                         @RequestParam(defaultValue = "relevant") String sortType,
                         @RequestParam(defaultValue = "1") int pageNum, Map<String, Object> model) {

        PageDto page = searchIndexService.searchIndex(BODY, q, sortType, pageNum);
        model.put("page", page);
        model.put("currentPage", pageNum);

        return "searchedPages";
    }
}
