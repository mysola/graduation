package com.wangyang.service;

import com.wangyang.entity.AnonymousUrlClick;
import com.wangyang.entity.RealNameUrlClick;


public interface UserService {

    AnonymousUrlClick getAnonymousUrlClick(String url);

    RealNameUrlClick getRealNameUrlClick(String username,String url);



}
