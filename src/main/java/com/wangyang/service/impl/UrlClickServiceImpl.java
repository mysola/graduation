package com.wangyang.service.impl;

import com.wangyang.mapper.AnonymousUrlClickDao;
import com.wangyang.mapper.RealNameUrlClickDao;
import com.wangyang.entity.AnonymousUrlClick;
import com.wangyang.entity.RealNameUrlClick;
import com.wangyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UrlClickServiceImpl implements UserService {


    @Autowired
    private AnonymousUrlClickDao anonymousUrlClickDao;

    @Autowired
    private RealNameUrlClickDao realNameUrlClickDao;


    @Override
    public AnonymousUrlClick getAnonymousUrlClick(String url) {
        return anonymousUrlClickDao.getUrlClick(url);
    }

    @Override
    public RealNameUrlClick getRealNameUrlClick(String username,String url) {
        return realNameUrlClickDao.getUrlClick(username,url);
    }
}
