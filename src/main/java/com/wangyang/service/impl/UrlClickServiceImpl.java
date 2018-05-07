package com.wangyang.service.impl;

import com.wangyang.docIndex.Searcher;
import com.wangyang.entity.UrlClick;
import com.wangyang.mapper.UrlClickDao;
import com.wangyang.pageRank.PageRanker;
import com.wangyang.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class UrlClickServiceImpl implements UserService {

    @Autowired
    private UrlClickDao urlClickDao;

    private static final String ANONYMOUS_STR = "anonymousUser";

    private static final String REAL_NAME_CLICK_KEY = "realNameClick";

    private static Log queryLog = LogFactory.getLog("queryLog");

    private static Log clickLog = LogFactory.getLog("clickLog");


    @Autowired
    private Searcher searcher;

    @PostConstruct
    public void init() throws IOException {
        //读取pagerank
        PageRanker pageRanker = new PageRanker();
        Map<String,Double> pageRank = pageRanker.readPR();
        //设置pagerank
        searcher.setPageRank(pageRank);
        //设置匿名点击
        searcher.setAnonymousUrlClick(urlClickDao.getAnonymousUrlClick());

    }

    @Override
    public UrlClick search(String queryStr) {
        String username = getUsername();
        if(username!=null&&username.equals(ANONYMOUS_STR)){
            searcher.setRealNameUrlClick(null);
        }
        else {
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder
                    .getRequestAttributes()).getRequest().getSession(false);
            if(session!=null){
                Map<String,UrlClick> realNameClick = (Map<String,UrlClick>)session
                        .getAttribute(REAL_NAME_CLICK_KEY);
                if(realNameClick==null){
                    realNameClick = urlClickDao.getRealNameUrlClick(username);
                    session.setAttribute(REAL_NAME_CLICK_KEY,realNameClick);
                }
                searcher.setRealNameUrlClick(realNameClick);
            }
        }
        queryLog.info(username+" "+queryStr);
        searcher.search(queryStr);
        return null;
    }

    private String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if(authentication!=null){
            username = authentication.getName();
        }
        return username;
    }

    @Override
    public String link(String linkUrl) {
        String username = getUsername();
        clickLog.info(username+""+linkUrl);
        return null;
    }
}
