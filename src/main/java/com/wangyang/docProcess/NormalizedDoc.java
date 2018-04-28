package com.wangyang.docProcess;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NormalizedDoc {
    private String url;

    private String keywords;
    private String description;
    private String title;
    private String charset;

    private String text;

    @JsonIgnore
    private String[] innerUrls;

    public NormalizedDoc() {
    }

    public NormalizedDoc(String keywords, String description, String title, String charset) {
        this.keywords = keywords;
        this.description = description;
        this.title = title;
        this.charset = charset;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String[] getInnerUrls() {
        return innerUrls;
    }

    public void setInnerUrls(String[] innerUrls) {
        this.innerUrls = innerUrls;
    }
}
