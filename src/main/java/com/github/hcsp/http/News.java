package com.github.hcsp.http;

import java.util.Date;

public class News {

    private Integer id;
    private  String url;
    private String content;
    private String title;
    private Date createdAt;
    private  Date modifiedAt;

    public News() {
    }

    public News(Integer id, String url, String content, String title, Date createdAt, Date modifiedAt) {
        this.id = id;
        this.url = url;
        this.content = content;
        this.title = title;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
