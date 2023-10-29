package com.qw.adse.ui.choujiang.bena;

import java.io.Serializable;
import java.util.List;

public class DrawBean implements Serializable {
    public int id;
    public String name;
    public String listName;
    private List<StringBean> list;

    public static class StringBean  implements Serializable {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StringBean> getList() {
        return list;
    }

    public void setList(List<StringBean> list) {
        this.list = list;
    }
}
