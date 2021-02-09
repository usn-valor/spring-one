package ru.home.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // аналог сервлета, обрабатывающий соответствующий URL
@RequestMapping("/")
public class StartPageController {

    private static final Logger logger = LoggerFactory.getLogger(StartPageController.class);

    @GetMapping
    public String startPage(Model model) {
        logger.info("Start page requested");
        return "index"; // возвращается название html-документа, который сверстает страницу согласно этого запроса
    }
}
