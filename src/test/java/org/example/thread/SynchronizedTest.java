package org.example.thread;

import org.example.util.JOLUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class SynchronizedTest {
    /**
     * 测试偏向锁
     * -XX:BiasedLockingStartupDelay=0
     */
    @Test
    public void testBiasedLock() {
        // thread id为空，锁状态为待偏向，是jvm新建对象时的优化设置
        JOLUtil.printJavaObjectLayout(this);
        syncArea();
        // 退出同步区后，线程不会主动释放偏向锁，方便下次直接访问同步区
        JOLUtil.printJavaObjectLayout(this);
        syncArea();
    }

    /**
     * 测试偏向锁升级为轻量级锁的场景
     * -XX:BiasedLockingStartupDelay=0
     */
    @Test
    public void testBiasedLockContentionUpgradeThinLock() throws InterruptedException {
        Thread threadA = new Thread(() -> {
            JOLUtil.printJavaObjectLayout(this);
            syncArea();
            // 线程b竞争偏向锁，线程a被挂起，偏向锁升级为轻量级锁
            Thread threadB = new Thread(this::syncArea);
            threadB.start();
            try {
                threadB.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        threadA.start();
        threadA.join();
    }

    /**
     * 测试当调用一个锁对象的wait方法时，如当前锁的状态是偏向锁则会先膨胀成重量级锁
     * -XX:BiasedLockingStartupDelay=0
     */
    @Test
    public void testBiasedLockUpgradeFatLockByWait() throws InterruptedException {
        JOLUtil.printJavaObjectLayout(this);
        synchronized (this) {
            JOLUtil.printJavaObjectLayout(this);
            this.wait(10);
            JOLUtil.printJavaObjectLayout(this);
        }
    }

    /**
     * 测试轻量级锁，
     */
    @Test
    public void testThinLock() {
        // jvm延迟开启偏向锁，锁状态为无锁
        JOLUtil.printJavaObjectLayout(this);
        syncArea();
        // 退出同步区后，线程主动释放轻量级锁，恢复到无锁状态
        JOLUtil.printJavaObjectLayout(this);
    }

    /**
     * 测试存在轻量级锁竞争时，线程b通过自旋成功等到线程a释放轻量级锁，避免轻量级锁升级为重量级锁
     * （由于没有延长线程b自旋时间的参数，线程b仍有很大概率自旋失败，导致轻量级锁升级为重量级锁）
     * （也有可能在线程b请求锁前，线程a已经从同步区退出，则线程b是在没有锁竞争的情况下直接获得轻量级锁）
     */
    @Test
    public void testThinLockSpinSuccess() throws InterruptedException {
        JOLUtil.printJavaObjectLayout(this);
        // 线程a进入空的同步块，提高线程b通过自旋获取到锁的概率
        Thread threadA = new Thread(this::syncAreaEmpty);
        Thread threadB = new Thread(this::syncArea);
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        JOLUtil.printJavaObjectLayout(this);
    }

    /**
     * 测试存在轻量级锁竞争时，线程a长时间持有轻量级锁，线程b自旋失败，最终轻量级锁升级为重量级锁
     */
    @Test
    public void testThinLockSpinFailUpgradeFatLock() throws InterruptedException {
        JOLUtil.printJavaObjectLayout(this);
        Thread threadA = new Thread(this::syncArea);
        Thread threadB = new Thread(this::syncArea);
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        JOLUtil.printJavaObjectLayout(this);
    }

    /**
     * 测试当调用一个锁对象的wait方法时，如当前锁的状态是轻量级锁则会先膨胀成重量级锁
     */
    @Test
    public void testThinLockUpgradeFatLockByWait() throws InterruptedException {
        JOLUtil.printJavaObjectLayout(this);
        synchronized (this) {
            JOLUtil.printJavaObjectLayout(this);
            this.wait(10);
            JOLUtil.printJavaObjectLayout(this);
        }
    }

    /**
     * 测试调用hashCode后，轻量级锁升级为重量级锁
     */
    @Test
    public void testThinLockUpgradeFatLockAfterHashCode() {
        synchronized (this) {
            JOLUtil.printJavaObjectLayout(this);
            System.out.println(this.hashCode());
            JOLUtil.printJavaObjectLayout(this);
        }
    }

    /**
     * 测试调用hashCode后，偏向锁升级为重量级锁
     * -XX:BiasedLockingStartupDelay=0
     */
    @Test
    public void testBiasedLockUpgradeFatLockAfterHashCode() {
        synchronized (this) {
            JOLUtil.printJavaObjectLayout(this);
            System.out.println(this.hashCode());
            JOLUtil.printJavaObjectLayout(this);
        }
    }

    /**
     * 同步区
     *
     * @param countDownLatch 协作
     */
    private synchronized void syncArea(CountDownLatch countDownLatch) {
        JOLUtil.printJavaObjectLayout(this);
        countDownLatch.countDown();
    }

    /**
     * 同步区
     */
    private synchronized void syncArea() {
        JOLUtil.printJavaObjectLayout(this);
    }

    /**
     * 同步区
     */
    private synchronized void syncAreaEmpty() {
        System.out.println();
    }
}
