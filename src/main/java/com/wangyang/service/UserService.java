package com.wangyang.service;

import com.wangyang.entity.UrlClick;


public interface UserService {

    UrlClick search(String queryStr);

    String link(String linkUrl);
}
