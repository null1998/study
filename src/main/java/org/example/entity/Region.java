package org.example.entity;

public class Region {

    public Region() {
    }

    public Region(Integer id, String name, Integer num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    private Integer id;

    private String name;

    private Integer num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}