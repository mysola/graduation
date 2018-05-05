package com.wangyang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "index")
    String getIndex(){
        return "Hello Spring Boot!";
    }

    @RequestMapping(value = "searchPage")
    String getSearch(){
        return "Hello Spring Boot!";
    }
}
