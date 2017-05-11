package com.chenguangl.simplegallery.data;

import java.io.Serializable;

public class Photo implements Serializable {

    private String name;
    private String uri;

    public Photo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
