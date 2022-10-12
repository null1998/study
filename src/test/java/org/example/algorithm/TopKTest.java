package org.example.algorithm;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class TopKTest {

    public static final int NUM = 1_000_000_000;

    @Test
    public void testTopKByMaxHeap() {
        LocalDateTime time = LocalDateTime.now();
        MaxHeap maxHeap = new MaxHeap(10);
        for (int i = 0; i < NUM; i++) {
            long timeStamp = ThreadLocalRandom.current().nextLong(LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli(),
                    LocalDateTime.of(2022, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.of("+8"));
            maxHeap.insert(localDateTime);
        }
        long total = ChronoUnit.MILLIS.between(time, LocalDateTime.now());
        System.out.println("总耗时" + total);

        LocalDateTime time1 = LocalDateTime.now();
        for (int i = 0; i < NUM; i++) {
            long timeStamp = ThreadLocalRandom.current().nextLong(LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli(),
                    LocalDateTime.of(2022, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.of("+8"));
        }
        long create = ChronoUnit.MILLIS.between(time1, LocalDateTime.now());
        System.out.println("构建对象耗时" + create);

        LocalDateTime time2 = LocalDateTime.now();
        for (int i = 0; i < NUM; i++) {

        }
        long loop = ChronoUnit.MILLIS.between(time2, LocalDateTime.now());
        System.out.println("循环耗时" + loop);
        System.out.println("净耗时" + (total - create + loop));
    }

    @Test
    public void testTopKBySort() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime[] array = new LocalDateTime[NUM];
        for (int i = 0; i < NUM; i++) {
            long timeStamp = ThreadLocalRandom.current().nextLong(LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli(),
                    LocalDateTime.of(2022, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.of("+8"));
            array[i] = localDateTime;
        }
        System.out.println("耗时" + ChronoUnit.MILLIS.between(time, LocalDateTime.now()));
        LocalDateTime time1 = LocalDateTime.now();
        Arrays.sort(array);
        for (int i = 0; i < 10; i++) {
            System.out.println(array[i].format(DateTimeFormatter.ISO_DATE_TIME));
        }
        System.out.println("耗时" + ChronoUnit.MILLIS.between(time1, LocalDateTime.now()));
    }
}

class MaxHeap {
    private final int limit;
    private final LocalDateTime[] maxHeap;
    private int size;

    public MaxHeap(int limit) {
        this.limit = limit;
        this.maxHeap = new LocalDateTime[limit + 1];
    }

    public void insert(LocalDateTime localDateTime) {
        if (size < limit) {
            size++;
            swim(localDateTime);
        } else if (maxHeap[1].isAfter(localDateTime)) {
            sink(localDateTime);
        }
    }

    private void sink(LocalDateTime localDateTime) {
        maxHeap[1] = localDateTime;
        int k = 1;
        while (2 * k <= size) {
            int next = 2 * k + 1 > size ? 2 * k : (after(2 * k, 2 * k + 1) ? 2 * k : 2 * k + 1);
            if (after(next, k)) {
                swap(next, k);
                k = next;
            } else {
                break;
            }
        }
    }

    private void swim(LocalDateTime localDateTime) {
        maxHeap[size] = localDateTime;
        int k = size;
        while (k > 1 && after(k, k / 2)) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    private void swap(int a, int b) {
        LocalDateTime temp = maxHeap[a];
        maxHeap[a] = maxHeap[b];
        maxHeap[b] = temp;
    }

    private boolean after(int a, int b) {
        return maxHeap[a].isAfter(maxHeap[b]);
    }

    public void print() {
        Arrays.sort(maxHeap, 1, size + 1);
        for (int i = 1; i < maxHeap.length; i++) {
            System.out.println(maxHeap[i].format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }
}
