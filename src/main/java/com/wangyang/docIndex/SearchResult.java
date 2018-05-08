package com.wangyang.docIndex;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchResult {
    private String title;
    private String content;
    @JsonIgnore
    private String query;
    @JsonIgnore
    private String url;

    private String link;


    public SearchResult(String title, String content, String query, String url) {
        this.title = title;
        this.content = content;
        this.query = query;
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQuery() {

        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
