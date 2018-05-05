package com.wangyang.mapper;

import com.wangyang.entity.UrlClick;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UrlClickDao {

    @MapKey("url")
    Map<String,UrlClick> getRealNameUrlClick(@Param("username") String username);

    @MapKey("url")
    Map<String,UrlClick> getAnonymousUrlClick();


}
