package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final PageParseService pageParseService;
    private final IndexService indexService;

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @PostMapping("/index")
    public String createIndex(@RequestParam
                              @NotBlank
                              @Size(min = 5, max = 200, message = "Url must contain from 5 to 200 symbols") String validUrl) {
        pageParseService.parsePageByUrl(2, Set.of(validUrl));
        indexService.index(pageParseService.getPages());

        return "indexComplete";
    }
}
