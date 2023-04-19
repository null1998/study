package org.example.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.ListUtils;
import org.example.entity.Book;
import org.example.entity.BookItem;
import org.example.entity.Item;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    /**
     * 测试abs(a)%100和abs(a%100)结果是否相同
     * m%n=m-(m/n)*n
     *
     */
    @Test
    public void testNegativeMod() {
        int limit = 10000000;
        Random random = new Random(limit);
        for (int i = 0; i < limit; i++) {
            int a = random.nextInt();
            Assertions.assertEquals(Math.abs(a) % 100, Math.abs(a % 100));
            System.out.println(String.format("%s  %s  %s", a, Math.abs(a) % 100, Math.abs(a % 100)));
        }
    }

    @Test
    public void testSetsIntersection() {
        Set<String> set1 = Sets.newHashSet("1");
        Set<String> set2 = Sets.newHashSet("1", "2");
        Sets.SetView<String> set3 = Sets.intersection(set1, set2);
        Assertions.assertEquals(1, set3.size());
        Assertions.assertTrue(set3.contains("1"));

        set2.removeAll(set3);
        Assertions.assertTrue(set3.isEmpty());
    }
}
