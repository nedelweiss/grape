package com.github.kolegran.grape.index;

import lombok.RequiredArgsConstructor;
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
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Validated
public class IndexController {
    private final PageParser pageParser;
    private final IndexService indexService;

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @PostMapping("/index")
    public String createIndex(@RequestParam
                              @NotBlank
                              @NotNull
                              @Size(min = 5, max = 200, message = "Url must contain from 5 to 200 symbols") String validUrl,
                              @RequestParam(defaultValue = "2") @Min(1) @Max(20) int indexDepth) {

        Map<String, ParsePageDto> pages = pageParser.parsePageByUrl(indexDepth, Set.of(validUrl));
        indexService.indexDocument(pages);

        return "indexed";
    }
}
