package com.wangyang.service;

import com.wangyang.docIndex.SearchResult;


public interface UserService {

    SearchResult[] search(String queryStr, int pageNum);

    String link(String linkUrl);
}
