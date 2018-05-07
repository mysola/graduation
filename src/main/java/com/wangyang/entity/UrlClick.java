package com.wangyang.entity;

public class UrlClick {
    private String url;

    private Integer anonymousClick;

    private String username;

    private Integer realNameClick;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAnonymousClick() {
        return anonymousClick;
    }

    public void setAnonymousClick(Integer anonymousClick) {
        this.anonymousClick = anonymousClick;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRealNameClick() {
        return realNameClick;
    }

    public void setRealNameClick(Integer realNameClick) {
        this.realNameClick = realNameClick;
    }
}
