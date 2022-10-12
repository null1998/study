package org.example.collection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class HashMapTest {
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            8,
            16,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(32),
            (Runnable r) -> {
                Thread t = new Thread(r);
                t.setName("hashmap-test-" + t.getId());
                return t;
            },
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 默认初始化容量为16，kv数量大于容量的0.75倍时，容量以乘以2的方式扩容
     */
    @Test
    public void testExpansion() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Map<Integer, Integer> map = new HashMap<>();
        Class<?> mapType = map.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        // 默认容量为16
        assert (int) capacity.invoke(map) == 16;

        Method loadFactor = mapType.getDeclaredMethod("loadFactor");
        loadFactor.setAccessible(true);
        // 默认负载因子为0.75
        assert (float) loadFactor.invoke(map) == 0.75;

        Field table = mapType.getDeclaredField("table");
        table.setAccessible(true);
        // 数组延迟到第一次put才分配空间
        assert table.get(map) == null;
        map.put(1, 1);
        assert table.get(map) != null;

        for (int i = 2; i <= 16; i++) {
            map.put(i, i);
            assert i != 12 || (int) capacity.invoke(map) == 16;
            // 元素个数大于容量乘以负载因子时，容量扩大为原来的两倍
            assert i != 13 || (int) capacity.invoke(map) == 32;
        }
    }

    /**
     * 指定初始化容量时，调整为第一个大等于指定值的2的幂次方
     */
    @Test
    public void testAssignInitCapacity() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> map = new HashMap<>(5);
        Class<?> mapType = map.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        assert (int) capacity.invoke(map) == 8;
    }

    /**
     * hashmap的hash函数为hashCode的低16位与高16位进行^运算
     */
    @Test
    public void testHash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String key = "abc";
        Map<String, String> map = new HashMap<>();
        Class<?> mapType = map.getClass();
        Method hash = mapType.getDeclaredMethod("hash", Object.class);
        hash.setAccessible(true);
        int h = key.hashCode();
        assert (int) hash.invoke(map, key) == (h ^ (h >>> 16));
    }

    /**
     * 计算index时，hash对2的幂次方的容量取模，等价于hash和2的幂次方的容量减1进行&运算
     */
    @Test
    public void testIndexFor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String key = "abc";
        Map<String, String> map = new HashMap<>();
        Class<?> mapType = map.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        Method hash = mapType.getDeclaredMethod("hash", Object.class);
        hash.setAccessible(true);
        int h = (int) hash.invoke(map, key);
        int c = (int) capacity.invoke(map);
        assert (h % c) == (h & (c - 1));
    }

    /**
     * rehash计算index时，原容量为2的n次方，hash的n+1位（或者说是判断hash&原容量==0）
     * 如果是0，index维持不变，如果是1，index为原index+原容量
     */
    @Test
    public void testRehashIndexFor() {
        // hash为11000010000000101
        int h1 = "def".hashCode() ^ ("def".hashCode() >>> 16);
        // hash为10111100001100010
        int h2 = "abc".hashCode() ^ ("abc".hashCode() >>> 16);
        // 模拟capacity从2扩容到4
        assert (h1 & (2 - 1)) == (h1 & (4 - 1));
        assert ((h2 & (2 - 1)) + 2) == (h2 & (4 - 1));
    }

    /**
     * put并发时，相同位置的键值对可能会被覆盖
     */
    @Test
    public void testThreadUnSafePut() throws InterruptedException {
        Map<String, Integer> map = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable threadUnsafePutTask = () -> {
            for (int i = 0; i < 100; i++) {
                map.put(Thread.currentThread().getName() + "-" + i, i);
            }
            countDownLatch.countDown();
        };
        threadPoolExecutor.execute(threadUnsafePutTask);
        threadPoolExecutor.execute(threadUnsafePutTask);
        countDownLatch.await();
        assert map.size() != 200;
    }

    /**
     * put和get并发时，在rehash过程中，get可能为null
     */
    @Test
    public void testThreadUnSafeGet() throws InterruptedException {
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = new ConcurrentSkipListSet<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        threadPoolExecutor.execute(() -> {
            for (int i = 0; i < 100000; i++) {
                map.put(i, i);
                set.add(i);
            }
            countDownLatch.countDown();
        });
        Future<?> future = threadPoolExecutor.submit(() -> {
            for (int i = 0; i < 100000; i++) {
                if (set.contains(i) && map.get(i) == null) {
                    countDownLatch.countDown();
                    throw new RuntimeException("get null");
                }
            }
            countDownLatch.countDown();
        });
        countDownLatch.await();
        try {
            future.get();
        } catch (Exception e) {
            assert "java.lang.RuntimeException: get null".equals(e.getMessage());
        }
    }
}
