package org.example.collection;


import org.assertj.core.util.Lists;
import org.example.entity.Order;
import org.example.entity.Region;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huang
 */
public class StreamTest {
    /**
     * 测试stream归约
     * 将一个stream中的所有元素反复结合起来，得到一个结果，这样的操作被称为归约
     */
    @Test
    public void testReduce() {
        Assertions.assertEquals(6, Lists.newArrayList(1, 2, 3).stream().reduce(Integer::sum).orElse(0));
        Assertions.assertEquals(3, Lists.newArrayList(1, 2, 3).stream().max(Integer::compare).orElse(0));
        Assertions.assertEquals(1, Lists.newArrayList(1, 2, 3).stream().min(Integer::compare).orElse(0));
        ArrayList<Order> orderList = Lists.newArrayList(
                new Order("01", "1", "a", new BigDecimal("1")),
                new Order("01", "2", "b", new BigDecimal("2")),
                new Order("02", "3", "c", new BigDecimal("4")));
        // 提取字段求和
        Assertions.assertEquals(new BigDecimal("7"), orderList.stream().map(Order::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 分组求和
        Map<String, BigDecimal> groupSumMap = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderType,
                        Collectors.reducing(BigDecimal.ZERO, Order::getAmt, BigDecimal::add)));
        Assertions.assertEquals(new BigDecimal("3"), groupSumMap.get("01"));
        Assertions.assertEquals(new BigDecimal("4"), groupSumMap.get("02"));
        // 多级分组
        Map<String, Map<String, List<Order>>> multiLevelGroupMap = orderList.stream()
                .collect(Collectors.groupingBy(Order::getOrderType, Collectors.groupingBy(Order::getOrderId)));
        Assertions.assertEquals(2, multiLevelGroupMap.size());
        Assertions.assertEquals(2, multiLevelGroupMap.get("01").size());
        Assertions.assertEquals(1, multiLevelGroupMap.get("02").size());
        Assertions.assertEquals("a", multiLevelGroupMap.get("01").get("1").get(0).getOrderName());
        Assertions.assertEquals("b", multiLevelGroupMap.get("01").get("2").get(0).getOrderName());
        Assertions.assertEquals("c", multiLevelGroupMap.get("02").get("3").get(0).getOrderName());
    }

    /**
     * 测试stream收集器
     */
    @Test
    public void testCollectors() {
        List<Region> regionList = Lists.newArrayList(new Region(1, "beijing", 1), new Region(2, "fujian", 1));
        // 想让一个id对应一个region，应该使用toMap
        Map<Integer, String> regionMap = regionList.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        Assertions.assertEquals("beijing", regionMap.get(1));
        Assertions.assertEquals("fujian", regionMap.get(2));

        ArrayList<Order> orderList = Lists.newArrayList(
                new Order("01", "1", "a", new BigDecimal("1")),
                new Order("01", "2", "b", new BigDecimal("2")),
                new Order("02", "3", "c", new BigDecimal("3")));
        // 想对数据进行分类，并且指定的key是可以重复的，应该使用groupingBy，toSet是可选参数
        Map<String, Set<Order>> orderMap = orderList.stream().collect(Collectors.groupingBy(Order::getOrderType, Collectors.toSet()));
        Assertions.assertEquals(2, orderMap.get("01").size());
        Assertions.assertEquals("c", orderMap.get("02").stream().findFirst().orElse(new Order()).getOrderName());

        // 将数据按照true或者false进行分组就叫做分区
        Map<Boolean, Set<Order>> partitionMap = orderList.stream().collect(Collectors.partitioningBy((order) -> Objects.equals(order.getOrderType(), "01"), Collectors.toSet()));
        Assertions.assertEquals(2, partitionMap.get(true).size());
        Assertions.assertEquals("c", partitionMap.get(false).stream().findFirst().orElse(new Order()).getOrderName());

        // 对订单进行分组，并找出每组有多少个订单
        Map<String, Long> countingMap = orderList.stream().collect(Collectors.groupingBy(Order::getOrderType, Collectors.counting()));
        Assertions.assertEquals(2, countingMap.get("01"));
        Assertions.assertEquals(1, countingMap.get("02"));

        // 对订单进行分组，并找出每组订单金额最大的那个
        Map<String, Optional<Order>> maxMap = orderList.stream().collect(Collectors.groupingBy(Order::getOrderType, Collectors.maxBy(Comparator.comparing(Order::getAmt))));
        Assertions.assertEquals(new BigDecimal("2"), maxMap.get("01").orElse(new Order()).getAmt());
        Assertions.assertEquals(new BigDecimal("3"), maxMap.get("02").orElse(new Order()).getAmt());
    }
}
