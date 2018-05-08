package com.wangyang.controller;

import com.wangyang.docIndex.SearchResult;
import com.wangyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = "application/json")
public class DataController {

    @Autowired
    private UserService urlClickService;

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    SearchResult[] search(@RequestParam(value = "queryStr") String queryStr,
                  @RequestParam(value = "pageNum") Integer pageNum){
        return urlClickService.search(queryStr,pageNum);
    }

    @RequestMapping(value = "/link",method = RequestMethod.GET)
    String link(@RequestParam(value = "linkUrl") String linkUrl){
        String redirectUrl = urlClickService.link(linkUrl);
        return "redirect:"+redirectUrl;
    }

}
