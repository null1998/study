package org.example.common;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.example.entity.Book;
import org.example.entity.BookItem;
import org.example.entity.Item;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huang
 */
public class CommonTest {
    @Test
    public void testBeanUtilShallowClone() {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book("id", "name", new BookItem(Lists.newArrayList(new Item("1", "a"),new Item("2", "b"),new Item("3", "c"))));
        List<List<Item>> partition = ListUtils.partition(book.getBookItem().getItemList(), 1);
        for (List<Item> itemList : partition) {
            Book copy = new Book();
            BeanUtils.copyProperties(book, copy);
            copy.getBookItem().setItemList(itemList);
            bookList.add(copy);
        }

        for (Book value : bookList) {
            Assertions.assertEquals("id", value.getId());
            Assertions.assertEquals("name", value.getName());
            Assertions.assertEquals("3", value.getBookItem().getItemList().get(0).getId());
            Assertions.assertEquals("c", value.getBookItem().getItemList().get(0).getName());
        }
    }
}
