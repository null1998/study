package org.example.entity;

import java.util.List;

/**
 * @author huang
 */
public class BookItem {
    private List<Item> itemList;

    public BookItem() {
    }

    public BookItem(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "BookItem{" +
                "itemList=" + itemList +
                '}';
    }
}
