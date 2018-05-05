package com.wangyang.mapper;

import com.wangyang.entity.RealNameUrlClick;
import org.apache.ibatis.annotations.Param;

public interface RealNameUrlClickDao {
    RealNameUrlClick getUrlClick(@Param("username") String username,@Param("url") String url);
}
