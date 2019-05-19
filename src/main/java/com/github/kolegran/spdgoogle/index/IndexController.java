package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class IndexController {
    final private LinkPreprocessService linkPreprocessService;

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @PostMapping("/index")
    public String createIndex(@RequestParam String validUri) {
        linkPreprocessService.create(validUri);

        return "indexComplete";
    }
}
