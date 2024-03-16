package org.danil.lab21.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@Slf4j
public class TimeController {
    @GetMapping
    String index(Model model) {
        log.info("get html");
        model.addAttribute("time", new Date().toString());
        return "index";
    }
    @GetMapping(value = "/api", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    Date indexRest() {
        log.info("get json");
        return new Date();
    }
}
