package com.isucollegemapadmin.model;

public class Faculty {
    private String name, position, img_link;

    public Faculty() {
    }

    public Faculty(String name, String position, String imgLink) {
        this.name = name;
        this.position = position;
        this.img_link = imgLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
