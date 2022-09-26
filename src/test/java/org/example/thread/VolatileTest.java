package org.example.thread;

import org.junit.Test;

public class VolatileTest {
    private static boolean flag;

    private static volatile boolean volatileFlag;

    private int a = 0, b = 0, x = 0, y = 0;

    private volatile int va = 0, vb = 0, vx = 0, vy = 0;

    /**
     * 测试没有volatile修饰的变量导致的内存可见性问题
     */
    @Test
    public void testMemoryVisibilityProblem() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!flag) {
            }
        });
        thread.start();
        // 主线程等待子线程读取flag=false到本地副本后再修改flag为true
        Thread.sleep(100);
        flag = true;
        // 子线程陷入死循环，程序阻塞
        thread.join();
    }

    /**
     * 测试volatile修饰的变量保证了内存可见性
     */
    @Test
    public void testVolatileGuaranteeMemoryVisibility() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!volatileFlag) {
            }
        });
        thread.start();
        // 主线程等待子线程读取volatileFlag=false到本地副本后再修改volatileFlag为true
        Thread.sleep(100);
        volatileFlag = true;
        // 子线程读取到主线程修改volatileFlag的值，程序正常退出
        thread.join();
    }

    /**
     * 测试指令重排问题
     * 若没有指令重排序，则x和y至少一个为1，x和y同时为0则表示出现指令重排
     */
    @Test
    public void testInstructionReorderingProblem() throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            Thread threadA = new Thread(() -> {
                a = 1;
                x = b;
            });
            Thread threadB = new Thread(() -> {
                b = 1;
                y = a;
            });
            threadA.start();
            threadB.start();
            threadA.join();
            threadB.join();
            if (x == 0 && y == 0) {
                System.out.printf("第%d次出现了指令重排序%n", i);
                break;
            }
        }
    }

    /**
     * 测试volatile禁止指令重排
     * 使用volatile修饰四个变量禁止指令重排，x和y至少一个为1
     */
    @Test
    public void testVolatileForbidInstructionReordering() throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            va = 0;
            vb = 0;
            vx = 0;
            vy = 0;
            Thread threadA = new Thread(() -> {
                va = 1;
                vx = vb;
            });
            Thread threadB = new Thread(() -> {
                vb = 1;
                vy = va;
            });
            threadA.start();
            threadB.start();
            threadA.join();
            threadB.join();
            if (vx == 0 && vy == 0) {
                System.out.printf("第%d次出现了指令重排序%n", i);
                break;
            }
        }
    }
}

