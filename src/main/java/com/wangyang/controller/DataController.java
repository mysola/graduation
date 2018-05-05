package com.wangyang.controller;

import com.wangyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DataController {

    @Autowired
    private UserService urlClickService;

    public void setUrlClickService(UserService urlClickService) {
        this.urlClickService = urlClickService;
    }

    @RequestMapping(value = "search")
    String search(){

        return "Hello Spring Boot!";
    }

    @RequestMapping(value = "link")
    String link(){
        return "Hello Spring Boot!";
    }

}
