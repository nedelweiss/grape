package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Controller
@RequiredArgsConstructor
public class IndexController {
    final private LinkPreprocessService linkPreprocessService;

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @PostMapping("/index")
    public String createIndex(@RequestParam
                              @NotBlank
                              @Size(min = 5, max = 200, message = "Uri must contain from 5 to 200 symbols") String validUri) {
        linkPreprocessService.preprocessUri(validUri);

        return "indexComplete";
    }
}
