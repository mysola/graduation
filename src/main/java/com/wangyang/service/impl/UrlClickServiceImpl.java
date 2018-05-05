package com.wangyang.service.impl;

import com.wangyang.docIndex.Searcher;
import com.wangyang.entity.UrlClick;
import com.wangyang.mapper.UrlClickDao;
import com.wangyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@Transactional
public class UrlClickServiceImpl implements UserService {

    @Autowired
    private UrlClickDao UrlClickDao;

    private static final String ANONYMOUS_STR = "anonymousUser";

    @Autowired
    private Searcher searcher;

    @PostConstruct
    public void init(){
        searcher.setAnonymousUrlClick(UrlClickDao.getAnonymousUrlClick());
    }

    @Override
    public UrlClick search() {
        Authentication o = SecurityContextHolder.getContext().getAuthentication();
        Map<String,UrlClick> map;
        if(o.getPrincipal().equals(ANONYMOUS_STR)){
            searcher.setRealNameUrlClick(null);
        }
        else {
            searcher.setRealNameUrlClick(UrlClickDao.getRealNameUrlClick("wy"));
        }

        return null;
    }
}
