package com.github.kolegran.grape.index;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Set;

@Controller
@Validated
public class IndexController {

    private final WebsiteParser websiteParser;
    private final IndexService indexService;

    public IndexController(WebsiteParser websiteParser, IndexService indexService) {
        this.websiteParser = websiteParser;
        this.indexService = indexService;
    }

    @GetMapping("/index")
    public String getStartIndexPage() {
        return "index";
    }

    @PostMapping("/index")
    public String createIndex(
        @RequestParam @NotBlank @NotNull @Size(min = 5, max = 3000, message = "Url must contains from 5 to 3000 symbols") String url,
        @RequestParam(defaultValue = "2") @Min(1) @Max(20) int indexDepth
    ) {
        indexService.indexDocument(websiteParser.parseByUrl(indexDepth, Set.of(url), new HashMap<>()));
        return "indexed";
    }
}
