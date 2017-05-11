package com.chenguangl.simplegallery.data;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {

    private String name;
    private List<Photo> photoList;

    public Album() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
