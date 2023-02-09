package org.example.entity;

/**
 * @author huang
 */
public class Book {
    private String id;
    private String name;
    private BookItem bookItem;

    public Book() {
    }

    public Book(String id, String name, BookItem bookItem) {
        this.id = id;
        this.name = name;
        this.bookItem = bookItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bookItem=" + bookItem +
                '}';
    }
}
